import { headingDistanceTo, moveTo, normalizeHeading } from "geolocation-utils";
import { findRoute } from "../graphhopper";

export async function* emulateCarPath(params: {
    start: GpsCoord;
    end: GpsCoord;
    initSpdKmh: number;
    maxSpdKmh: number;
    acc: number;
}) {
    const route = await findRoute("car", params.start, params.end);
    const path = route.paths[0];

    let pidx = 0;
    let current = path.points.coordinates[0];
    let next = path.points.coordinates[pidx];
    let spdKmh = params.initSpdKmh;

    while (pidx < path.points.coordinates.length) {
        const hd = headingDistanceTo(current, next);
        const distanceKm = hd.distance / 1000;

        const spdMh = spdKmh / 3.6;
        let remainMeter = spdMh;

        if (distanceKm <= remainMeter) {
            const stopover = moveTo(current, { heading: hd.heading, distance: hd.distance });
            // @ts-expect-error: lon is not always defined
            current = { lat: stopover.lat, lng: stopover.lon || stopover.lng }
            next = path.points.coordinates[++pidx];
            remainMeter -= hd.distance;
            continue;
        }
        else {
            const nextPoint = moveTo(current, { heading: hd.heading, distance: spdMh });
            // @ts-expect-error: lon is not always defined
            current = { lat: nextPoint.lat, lng: nextPoint.lon || nextPoint.lng }
        }

        const spdDiffKmh = Math.random() * params.acc * 2 - params.acc * 0.6;
        spdKmh = Math.min(Math.max(0, spdKmh + spdDiffKmh), params.maxSpdKmh);

        yield {
            current,
            next,
            ang: normalizeHeading(hd.heading),
            spd: spdKmh,
        }
    }
}