import { SharedArray } from 'k6/data';

export const cars = new SharedArray('cars', () => {
    return JSON.parse(open('../data/cars.json')) as CarType[];
});
export const stations = new SharedArray('stations', () => {
    return JSON.parse(open('../data/stations.json')) as StationType[];
});

export function getCar() {
    const idx = __VU - 1;
    return cars[idx];
}

export function getStartStation() {
    const carIdx = __VU;
    const stationIdx = (carIdx) % stations.length;
    return stations[stationIdx];
}

export function getEndStation() {
    const carIdx = __VU;
    const stationIdx = (carIdx * 2) % stations.length;
    return stations[stationIdx];
}

export function getRandomStation() {
    const stationIdx = Math.floor(Math.random() * stations.length);
    return stations[stationIdx];
}
