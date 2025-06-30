import http from 'k6/http';
import type {Options} from 'k6/options';
import {cars, getCar, getEndStation} from '../lib/shared.ts';

import * as utils from '../lib/utils.ts';
import {sleep} from 'k6';

export const options: Options = {
    vus: cars.length,
    scenarios: {
        once: {
            executor: 'per-vu-iterations',
            iterations: 1,
        },
        forever: {
            executor: 'constant-vus',
            duration: "1000000h",
        }
    }
}

let prev: MdtLog | null = null;

export function setup() {
    return {
        API_HUB_URL: __ENV.API_HUB_URL
    };
}

export default function (data: ReturnType<typeof setup>) {
    const now = new Date();

    const car = getCar();
    const endStation = getEndStation();

    const oTme = new Date();

    // generate logs
    const logs = [] as MdtLog[]

    if (prev == null) {
        prev = {
            sec: 0,
            gcd: 'A',
            lat: car.latitude,
            lng: car.longitude,
            ang: 0,
            spd: 0,
            sum: 0,
            bat: 0
        }
    }

    for (let s = 0; s < 60; s++) {
        const hd = utils.headingDistanceTo(prev!, endStation);
        const spd = utils.nextCarSpd(prev!.spd);
        const ang = hd.heading;
        const sum = prev!.sum + spd;
        const {lat, lon} = utils.moveTo(prev!, {heading: hd.heading, distance: hd.distance});

        const log: MdtLog = {
            sec: s,
            gcd: 'A',
            lat: lat,
            lng: lon,
            ang: ang,
            spd: spd,
            sum: sum,
            bat: 20
        }
        logs.push(log);
        prev = log;

        if (utils.distanceTo(log, endStation) < 10) {
            __ENV.TURN_BACK = 'true';
        }
    }

    sleep(Math.random() * 5);

    http.post(
        `${data.API_HUB_URL}/api/cycle-log`,
        JSON.stringify({
            mdn: car.id,
            tid: `TID`,
            mid: `MID`,
            pv: 1,
            did: `DID`,
            oTime: utils.dateFormat(oTme, "yyyymmddHHMM"),
            cCnt: 60,
            cList: logs
        }),
        {
            headers: {
                "Content-Type": "application/json",
                'X-TUID': car.activeTransactionId,
                'X-Timestamp': utils.dateFormat(now, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )

    sleep(60);
}
