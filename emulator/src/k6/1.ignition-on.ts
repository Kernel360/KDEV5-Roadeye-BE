import http from 'k6/http';
import type {Options} from 'k6/options';
import {cars, getCar, getStartStation} from '../lib/shared.ts';
import * as utils from '../lib/utils.ts';

export const options: Options = {
    vus: cars.length,
    iterations: cars.length
}

export function setup() {
    return {
        API_HUB_URL: __ENV.API_HUB_URL
    };
}

export default function (data: ReturnType<typeof setup>) {
    const now = new Date();

    const car = getCar();
    const station = getStartStation();
    g
    const onTime = new Date();

    http.post(
        `${data.API_HUB_URL}/api/ignition/on`,
        JSON.stringify({
            mdn: car.id,
            tid: `TID`,
            mid: "MID",
            pv: 1,
            did: "DID",
            onTime: utils.dateFormat(onTime, "yyyymmddHHMMss"),
            gcd: 'A',
            lat: station.lat,
            lon: station.lng,
            ang: 0,
            spd: 0,
            sum: 0
        }),
        {
            headers: {
                "Content-Type": "application/json",
                'X-TUID': car.activeTransactionId,
                'X-Timestamp': utils.dateFormat(now, "yyyy-mm-dd HH:MM:ss.l")
            }
        }
    )
}
