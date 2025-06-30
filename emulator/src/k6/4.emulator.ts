import http from 'k6/http';
import type {Options} from 'k6/options';
import {getCar, getEndStation, getRandomStation, getStartStation} from '../lib/shared.ts';
import * as utils from '../lib/utils.ts';
import type CarType from '../data/car.example.json';
import {sleep} from 'k6';

enum Phase {
    INIT,
    IGNITION_ON,
    DRIVING,
    IGNITION_OFF,
    IDLE
}

type Context = {
    phase: Phase
    car: typeof CarType & {
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
    transactionId: string | null;
    onTime: Date | null;
    offTime: Date | null;
    logs: MdtLog[];
    prev: MdtLog | null;
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

export default function (data: ReturnType<typeof setup>) {
    switch (ctx.phase) {
        case Phase.INIT:
            init(ctx);
            ctx.phase = Phase.IGNITION_ON;
            break;
        case Phase.IGNITION_ON:
            ignitionOn(ctx, data);
            ctx.phase = Phase.DRIVING;
            break;
        case Phase.DRIVING:
            driving(ctx, data);
            if (utils.distanceTo(ctx.location.current, ctx.location.end) < 10) {
                ctx.phase = Phase.IGNITION_OFF;
            }
            break;
        case Phase.IGNITION_OFF:
            ignitionOff(ctx, data);
            ctx.phase = Phase.IDLE;
            break;
        case Phase.IDLE:
            idle(ctx);
            ctx.phase = Phase.IGNITION_ON;
            break;
    }
}

function init(ctx: Context) {
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
    ctx.logs = [];
    ctx.prev = null;
}

function ignitionOn(ctx: Context, data: ReturnType<typeof setup>) {
    ctx.onTime = new Date();
    ctx.transactionId = utils.uuid().v4();

    http.post(
        `${data.API_HUB_URL}/api/ignition/on`,
        JSON.stringify({
            mdn: ctx.car.id,
            tid: `TID`,
            mid: "MID",
            pv: 1,
            did: "DID",
            onTime: utils.dateFormat(ctx.onTime, "yyyymmddHHMMss"),
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
                'X-Timestamp': utils.dateFormat(ctx.onTime, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )
}

function driving(ctx: Context, data: ReturnType<typeof setup>) {
    const now = new Date();

    if (ctx.prev == null) {
        ctx.prev = {
            sec: 0,
            gcd: 'A',
            lat: ctx.location.current.lat,
            lng: ctx.location.current.lng,
            ang: ctx.car.ang,
            spd: ctx.car.spd,
            sum: ctx.car.sum,
            bat: ctx.car.bat
        }
    }

    for (let s = 0; s < 60; s++) {
        const hd = utils.headingDistanceTo(ctx.prev!, ctx.location.end);
        const spd = utils.nextCarSpd(ctx.prev!.spd);
        const ang = hd.heading;
        const sum = ctx.prev.sum + spd;
        const {lat, lon} = utils.moveTo(ctx.prev, {heading: hd.heading, distance: hd.distance});

        const log: MdtLog = {
            sec: s,
            gcd: 'A',
            lat: lat,
            lng: lon,
            ang: ang,
            spd: spd,
            sum: sum,
            bat: ctx.car.bat
        }

        ctx.car.sum = sum;
        ctx.car.ang = ang;
        ctx.car.spd = spd;
        ctx.car.bat = ctx.car.bat - 0.01;

        ctx.logs.push(log);
        ctx.prev = log;
    }

    http.post(
        `${data.API_HUB_URL}/api/cycle-log`,
        JSON.stringify({
            mdn: ctx.car.id,
            tid: `TID`,
            mid: `MID`,
            pv: 1,
            did: `DID`,
            oTime: utils.dateFormat(now, "yyyymmddHHMM"),
            cCnt: ctx.logs.length,
            cList: ctx.logs
        }),
        {
            headers: {
                "Content-Type": "application/json",
                'X-TUID': ctx.transactionId!,
                'X-Timestamp': utils.dateFormat(now, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )

    sleep(60);
}

function ignitionOff(ctx: Context, data: ReturnType<typeof setup>) {
    const now = new Date();

    ctx.offTime = now;

    http.post(
        `${data.API_HUB_URL}/api/ignition/off`,
        JSON.stringify({
            mdn: ctx.car.id,
            tid: `TID`,
            mid: "MID",
            pv: 1,
            did: "DID",
            onTime: utils.dateFormat(ctx.onTime!, "yyyymmddHHMMss"),
            offTime: utils.dateFormat(ctx.offTime, "yyyymmddHHMMss"),
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
                'X-Timestamp': utils.dateFormat(now, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )
}

function idle(ctx: Context) {
    while (true) {
        const st = getRandomStation();
        if (utils.distanceTo(ctx.location.current, st) > 1000) {
            ctx.location.end = st;
            break;
        }
    }

    ctx.prev = null;
    ctx.onTime = null;
    ctx.offTime = null;
    ctx.transactionId = null;

    const minutes = Math.random() * 10 + 20;
    sleep(minutes);
}
