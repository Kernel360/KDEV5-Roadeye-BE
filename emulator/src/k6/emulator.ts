import dateFormat from 'dateformat';
import { distanceTo } from 'geolocation-utils';
import { check, sleep } from 'k6';
import http, { type RefinedResponse } from 'k6/http';
import type { Options } from 'k6/options';
import * as uuid from 'uuid';
import { emulateCarPath } from '~/lib/emulator/index.k6';
import { getCar, getEndStation, getStartStation } from '~/lib/shared.ts';

const Phase = {
    INIT: 'INIT',
    IGNITION_ON: 'IGNITION_ON', 
    DRIVING: 'DRIVING',
    IGNITION_OFF: 'IGNITION_OFF',
    IDLE: 'IDLE'
} as const;

type Phase = typeof Phase[keyof typeof Phase];

export const options: Options = {
    vus: 30,
    duration: "24h"
} as const;

const API_HUB_URL = __ENV.API_HUB_URL;

let emulator: Awaited<ReturnType<typeof emulateCarPath>> | null = null;

async function buildContext(vu: number) {
    const start = getStartStation();
    const end = getEndStation(start);

    const ctx = {
        vu,
        phase: Phase.INIT as Phase,
        car: {
            ...getCar(vu),
            lat: start.lat,
            lng: start.lon,
            ang: 0,
            spd: 0,
            sum: 0,
            bat: 20
        },
        location: {
            start,
            current: {
                lat: start.lat,
                lon: start.lon
            },
            end
        },
        transactionId: null as string | null,
        onTime: null as number | null,
        offTime: null as number | null
    };

        
    (() => {
        const res = http.get(`http://localhost:5678/cars/${ctx.car.id}`);
        if (res.status !== 200) {
            console.log(`Failed to get car info: ${res.status}`);
        }

        const json = JSON.parse(res.body?.toString() ?? "{}");
        ctx.car.sum = json.mileage_sum;
    })();

    return ctx;
}

type VuContext = Awaited<ReturnType<typeof buildContext>>;
type TestContext = Awaited<ReturnType<typeof setup>>;

export async function setup() {
    const context: Record<number, VuContext> = {};
    for (let vu = 1; vu <= options.vus!; vu++) {
        console.log(`Building context for vu-${vu}...`);

        context[vu] = await buildContext(vu);

        http.put(`http://localhost:5678/vus/${vu}`, JSON.stringify(context[vu]), {
            headers: {
                "Content-Type": "application/json",
            }
        })
    }

    return {
        context
    }
}

export async function main(data: TestContext) {
    const ctx = data.context[`${__VU}`];

    switch (ctx.phase) {
        case Phase.INIT:
            await init(ctx);
            ctx.phase = Phase.IGNITION_ON;
            break;
        case Phase.IGNITION_ON:
            await ignitionOn(ctx);
            ctx.phase = Phase.DRIVING;
            break;
        case Phase.DRIVING:
            await driving(ctx);
            if (distanceTo(ctx.location.current, ctx.location.end) < 10) {
                ctx.phase = Phase.IGNITION_OFF;
            }
            break;
        case Phase.IGNITION_OFF:
            await ignitionOff(ctx);
            ctx.phase = Phase.IDLE;
            break;
        case Phase.IDLE:
            await idle();
            ctx.phase = Phase.INIT;
            break;
    }
    
    http.put(`http://localhost:5678/vus/${ctx.vu}`, JSON.stringify(ctx), {
        headers: {
            "Content-Type": "application/json",
        }
    })
}

export function teardown(data: TestContext) {
    for (const { vu } of Object.values(data.context)) {
        console.log(`Tearing down vu-${vu}...`);

        const ctx = (() => {
            const res = http.get(`http://localhost:5678/vus/${vu}`);
            return JSON.parse(res.body?.toString() ?? "{}");
        })();

        if (ctx.phase === Phase.IDLE) {
            continue;
        }

        const now = Date.now();
        
        const res = http.request(
            "POST",
            `${API_HUB_URL}/api/ignition/off`,
            JSON.stringify({
                mdn: ctx.car.id,
                tid: `TID`,
                mid: "MID",
                pv: 1,
                did: "DID",
                onTime: dateFormat(ctx.onTime!, "yyyymmddHHMMss"),
                offTime: dateFormat(ctx.offTime, "yyyymmddHHMMss"),
                gcd: 'A',
                lat: ctx.location.current.lat,
                lon: ctx.location.current.lon,
                ang: ctx.car.ang,
                spd: ctx.car.spd,
                sum: ctx.car.sum
            }),
            {
                headers: {
                    "Content-Type": "application/json",
                    'X-TUID': ctx.transactionId!,
                    'X-Timestamp': dateFormat(now, "yyyy-mm-dd HH:MM:ss.l")
                }
            }
        )
        checkResponse(res);

        http.del(`http://localhost:5678/vus/${vu}`);
    }
}

function checkResponse(res: RefinedResponse<http.ResponseType | undefined>) {
    check(res, {
        'result success': (r) => {
            if (r.json("rstCd") !== "000") {
                console.log(`request failed: ${r.body?.toString() ?? "no body"}`)
            };
            return r.json("rstCd") === "000"
        },
    })
}

async function init(ctx: VuContext) {
    if (ctx.location.end != null) {
        ctx.location.start = ctx.location.end;
    }
    else {
        ctx.location.start = getStartStation();
    }
    ctx.location.end = getEndStation(ctx.location.start);
    ctx.location.current = {
        lat: ctx.location.start.lat,
        lon: ctx.location.start.lon
    }

    emulator = await emulateCarPath({
        start: ctx.location.start,
        end: ctx.location.end,
        initSpdKmh: 10,
        maxSpdKmh: 80,
        acc: 0.5,
    });

    ctx.car.spd = 0;
    ctx.car.ang = 0;
    ctx.onTime = null;
    ctx.offTime = null;
    ctx.transactionId = null;
}

async function ignitionOn(ctx: VuContext) {
    ctx.onTime = Date.now()
    ctx.transactionId = uuid.v4();

    const body = {
        mdn: ctx.car.id,
        tid: `TID`,
        mid: "MID",
        pv: 1,
        did: "DID",
        onTime: dateFormat(ctx.onTime, "yyyymmddHHMMss"),
        gcd: 'A',
        lat: ctx.location.current.lat,
        lon: ctx.location.current.lon,
        ang: ctx.car.ang,
        spd: ctx.car.spd,
        sum: ctx.car.sum
    }

    const res = await http.asyncRequest(
        "POST",
        `${API_HUB_URL}/api/ignition/on`,
        JSON.stringify(body),
        {
            headers: {
                "Content-Type": "application/json",
                'X-TUID': ctx.transactionId!,
                'X-Timestamp': dateFormat(ctx.onTime, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )
    checkResponse(res);
}

async function driving(ctx: VuContext) {
    const nLogs = 60;

    const now = new Date();

    const logs: MdtLog[] = [];
    let i = 0;
    while (i < nLogs) {
        const next = emulator!.next();
        if (next.done) break;
        const route = next.value;

        const log: MdtLog = {
            sec: i++,
            gcd: 'A',
            lat: route.current.lat,
            lon: route.current.lon,
            ang: route.ang,
            spd: route.spdKmh,
            sum: ctx.car.sum,
            bat: ctx.car.bat
        }
        logs.push(log);

        ctx.location.current = {
            lat: route.current.lat,
            lon: route.current.lon
        }
        ctx.car.lat = route.current.lat;
        ctx.car.lng = route.current.lon;
        ctx.car.sum = ctx.car.sum + route.sum;
        ctx.car.ang = route.ang;
        ctx.car.spd = route.spdKmh

        sleep(1);
    }

    if (logs.length === 0) {
        ctx.phase = Phase.IGNITION_OFF;
        return;
    }

    const res = await http.asyncRequest(
        "POST",
        `${API_HUB_URL}/api/cycle-log`,
        JSON.stringify({
            mdn: ctx.car.id,
            tid: `TID`,
            mid: `MID`,
            pv: 1,
            did: `DID`,
            oTime: dateFormat(now, "yyyymmddHHMM"),
            cCnt: logs.length,
            cList: logs
        }),
        {
            headers: {
                "Content-Type": "application/json",
                'X-TUID': ctx.transactionId!,
                'X-Timestamp': dateFormat(now, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )
    checkResponse(res);
}

async function ignitionOff(ctx: VuContext) {
    const now = Date.now();

    ctx.offTime = now;

    const res = await http.asyncRequest(
        "POST",
        `${API_HUB_URL}/api/ignition/off`,
        JSON.stringify({
            mdn: ctx.car.id,
            tid: `TID`,
            mid: "MID",
            pv: 1,
            did: "DID",
            onTime: dateFormat(ctx.onTime!, "yyyymmddHHMMss"),
            offTime: dateFormat(ctx.offTime, "yyyymmddHHMMss"),
            gcd: 'A',
            lat: ctx.location.current.lat,
            lon: ctx.location.current.lon,
            ang: ctx.car.ang,
            spd: ctx.car.spd,
            sum: ctx.car.sum
        }),
        {
            headers: {
                "Content-Type": "application/json",
                'X-TUID': ctx.transactionId!,
                'X-Timestamp': dateFormat(now, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )
    checkResponse(res);
}

async function idle() {
    emulator = null;
    
    const minutes = Math.random() * 5 + 3; // 3 to 8 minutes
    sleep(minutes);
}

export default main;