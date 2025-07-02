import dateFormat from 'dateformat';
import { distanceTo } from 'geolocation-utils';
import { sleep } from 'k6';
import http from 'k6/http';
import type { Options } from 'k6/options';
import * as uuid from 'uuid';
import { emulateCarPath } from '~/lib/emulator/index.k6';
import { getCar, getEndStation, getRandomStation, getStartStation } from '~/lib/shared.ts';

enum Phase {
    INIT,
    IGNITION_ON,
    DRIVING,
    IGNITION_OFF,
    IDLE
}

type Context = {
    phase: Phase
    car: {
        id: unknown;
        latitude: number;
        longitude: number;
        ang: number;
        spd: number;
        sum: number;
        bat: number;
    };
    location: {
        start: GpsCoord;
        current: GpsCoord;
        end: GpsCoord;
    };
    emulator: Awaited<ReturnType<typeof emulateCarPath>>
    transactionId: string | null;
    onTime: Date | null;
    offTime: Date | null;
}

// @ts-expect-error: INIT PHASE에서 초기화 될 것임.
const initialContext: Context = {
    phase: Phase.INIT,
}

const ctx: Context = initialContext;

export const options: Options = {
    vus: Number(__ENV.VUS) || 1,
    duration: "1000000h"
}

export function setup() {
    return {
        API_HUB_URL: __ENV.API_HUB_URL
    };
}

export function teardown() {

}

export default async function (data: ReturnType<typeof setup>) {
    switch (ctx.phase) {
        case Phase.INIT:
            await init(ctx);
            ctx.phase = Phase.IGNITION_ON;
            break;
        case Phase.IGNITION_ON:
            await ignitionOn(ctx, data);
            ctx.phase = Phase.DRIVING;
            break;
        case Phase.DRIVING:
            await driving(ctx, data);
            if (distanceTo(ctx.location.current, ctx.location.end) < 10) {
                ctx.phase = Phase.IGNITION_OFF;
            }
            break;
        case Phase.IGNITION_OFF:
            await ignitionOff(ctx, data);
            ctx.phase = Phase.IDLE;
            break;
        case Phase.IDLE:
            await idle(ctx);
            ctx.phase = Phase.IGNITION_ON;
            break;
    }
}

async function init(ctx: Context) {
    console.log(`[VU-${__VU}] init`);

    ctx.car = {
        ...getCar(),
        ang: 0,
        spd: 0,
        sum: 0,
        bat: 20
    }
    ctx.location = {
        start: getStartStation(),
        current: getStartStation(),
        end: getEndStation()
    };
    ctx.transactionId = null;
    ctx.onTime = null;
    ctx.offTime = null;
}

async function ignitionOn(ctx: Context, data: ReturnType<typeof setup>) {
    ctx.onTime = new Date();
    ctx.transactionId = uuid.v4();
    ctx.emulator = await emulateCarPath({
        start: ctx.location.start,
        end: ctx.location.end,
        initSpdKmh: 10,
        maxSpdKmh: 80,
        acc: 0.5,
    })

    sleep(10);

    await http.asyncRequest(
        "POST",
        `${data.API_HUB_URL}/api/ignition/on`,
        JSON.stringify({
            mdn: ctx.car.id,
            tid: `TID`,
            mid: "MID",
            pv: 1,
            did: "DID",
            onTime: dateFormat(ctx.onTime, "yyyymmddHHMMss"),
            gcd: 'A',
            lat: ctx.location.current.lat,
            lon: ctx.location.current.lng,
            ang: ctx.car.ang,
            spd: ctx.car.spd,
            sum: ctx.car.sum
        }),
        {
            headers: {
                "Content-Type": "application/json",
                'X-TUID': ctx.transactionId!,
                'X-Timestamp': dateFormat(ctx.onTime, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )

    sleep(10);
}

async function driving(ctx: Context, data: ReturnType<typeof setup>) {
    const nLogs = 60;

    const now = new Date();

    const logs: MdtLog[] = [];
    const i = 0;
    while (i < nLogs) {
        const next = ctx.emulator.next();
        if (next.done) break;
        const route = next.value;

        const log: MdtLog = {
            sec: i,
            gcd: 'A',
            lat: route.current.lat,
            lng: route.current.lng,
            ang: route.ang,
            spd: route.spd,
            sum: ctx.car.sum,
            bat: ctx.car.bat
        }
        logs.push(log);

        ctx.car.latitude = route.current.lat;
        ctx.car.longitude = route.current.lng;
        ctx.car.sum = ctx.car.sum + route.spd;
        ctx.car.ang = route.ang;
        ctx.car.spd = route.spd;

        sleep(1);
    }

    await http.asyncRequest(
        "POST",
        `${data.API_HUB_URL}/api/cycle-log`,
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
}

async function ignitionOff(ctx: Context, data: ReturnType<typeof setup>) {
    const now = new Date();

    ctx.offTime = now;

    await http.asyncRequest(
        "POST",
        `${data.API_HUB_URL}/api/ignition/off`,
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
            lon: ctx.location.current.lng,
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
}

async function idle(ctx: Context) {
    while (true) {
        const st = getRandomStation();
        if (distanceTo(ctx.location.current, st) > 1000) {
            ctx.location.end = st;
            break;
        }
    }

    ctx.onTime = null;
    ctx.offTime = null;
    ctx.transactionId = null;

    const minutes = Math.random() * 10 + 20;
    sleep(minutes);
}
