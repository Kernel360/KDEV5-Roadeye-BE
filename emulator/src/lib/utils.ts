import dateFormatjs from './vendor/dateformat.js';
import geoUtils from './vendor/geolocation-utils.js';
import uuidlib from './vendor/uuid.js';

export function nextCarSpd(spd: number, acc = 0.5, max = 80) {
    const ds = Math.random() * acc * 2 - acc * 0.6;
    return Math.min(Math.max(0, spd + ds), max);
}

export function uuid() {
    return {
        v4: () => {
            return uuidlib();
        }
    }
}

export function dateFormat(date: Date, format: string) {
    return dateFormatjs(date, format);
}

export function headingDistanceTo(prev: GpsCoord, endStation: GpsCoord) {
    const hd = geoUtils.headingDistanceTo(prev, endStation);
    return {
        heading: geoUtils.normalizeHeading(hd.heading),
        distance: hd.distance,
        __heading: hd.heading,
        __distance: hd.distance
    }
}

export function moveTo(prev: GpsCoord, { heading, distance }: { heading: number, distance: number }) {
    const loc = geoUtils.moveTo(prev, { heading, distance });
    return {
        lat: geoUtils.normalizeLatitude(loc.lat),
        lon: geoUtils.normalizeLongitude(loc.lon)
    }
}

export function distanceTo(prev: GpsCoord, endStation: GpsCoord) {
    return geoUtils.distanceTo(prev, endStation);
}
