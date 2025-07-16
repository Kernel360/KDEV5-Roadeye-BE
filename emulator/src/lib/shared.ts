import { SharedArray } from 'k6/data';

// const cars = new SharedArray('cars', () => {
//     return JSON.parse(open('../data/cars.local.json')) as CarType[];
// });
const stations = new SharedArray('stations', () => {
    return JSON.parse(open('../data/stations.json')) as StationType[];
});

export function getCar(vu: number) {
    // const idx = __VU - 1;
    // return cars[idx];
    return {
        id: vu + 2
    } as const;
}

export function getStartStation() {
    const stationIdx = Math.floor(Math.random() * stations.length);
    return stations[stationIdx];
}

export function getEndStation(start: StationType) {
    if (stations.length === 1) {
        throw new Error("Only one station");
    }

    do {
        const stationIdx = Math.floor(Math.random() * stations.length);
        const station = stations[stationIdx];
        if (station !== start) {
            return station;
        }
        // eslint-disable-next-line no-constant-condition
    } while (true);
}

export function getRandomStation() {
    const stationIdx = Math.floor(Math.random() * stations.length);
    return stations[stationIdx];
}
