import { headingDistanceTo, moveTo, normalizeHeading } from "geolocation-utils";
import { findRoute } from "../graphhopper";

export async function emulateCarPath(params: {
    start: GpsCoord;
    end: GpsCoord;
    initSpdKmh: number;
    maxSpdKmh: number;
    initMileage: number;
    acc: number;
}) {
    const route = await findRoute("car", params.start, params.end);
    const path = route.paths[0];

    let done = false;
    let pidx = 0;
    let current = path.points.coordinates[0];
    let next = path.points.coordinates[pidx + 1];
    let spdKmh = params.initSpdKmh;
    let remainMeter = spdKmh / 3.6;
    let mileage = params.initMileage;

    const getNext = () => {
        while (pidx < path.points.coordinates.length) {
            const hd = headingDistanceTo(current, next);

            if (hd.distance <= remainMeter) {
                const stopover = moveTo(current, { heading: hd.heading, distance: hd.distance });
                // @ts-expect-error: lon is not always defined
                current = { lat: stopover.lat, lon: stopover.lon || stopover.lon }
                next = path.points.coordinates[++pidx];
                remainMeter -= hd.distance;
                continue;
            }
            else {
                const spdMph = spdKmh / 3.6;
                const nextPoint = moveTo(current, { heading: hd.heading, distance: spdMph });
                // @ts-expect-error: lon is not always defined
                current = { lat: nextPoint.lat, lon: nextPoint.lon || nextPoint.lon }

                const spdDiff = Math.random() * params.acc * 2 - params.acc * 0.6;
                const nextSpd = Math.min(Math.max(0, spdKmh + spdDiff), params.maxSpdKmh);
                spdKmh = nextSpd;
                remainMeter = spdKmh;
            }

            mileage += spdKmh / 3.6;

            return {
                current,
                next,
                ang: normalizeHeading(hd.heading),
                spd: spdKmh,
                mileage,
            }
        }
        done = true;
        return null;
    }

    return {
        next: () => ({ value: getNext()!, done })
    }
}