import {SharedArray} from 'k6/data';

import type carType from '../data/car.example.json';

export const cars = new SharedArray('cars', () => {
    return JSON.parse(open('../data/cars.json')) as typeof carType[];
});
export const stations = new SharedArray('stations', () => {
    return JSON.parse(open('../data/stations.json')) as Station[];
});

export function getCar() {
    const idx = __VU - 1;
    return cars[idx];
}

export function getStartStation() {
    const carIdx = __VU - 1;
    let stationIdx = (carIdx) % stations.length;
    return stations[stationIdx];
}

export function getEndStation() {
    const carIdx = __VU - 1;
    let stationIdx = (carIdx * 2) % stations.length;
    return stations[stationIdx];
}

export function getRandomStation() {
    const stationIdx = Math.floor(Math.random() * stations.length);
    return stations[stationIdx];
}
