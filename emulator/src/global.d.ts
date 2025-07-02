declare module "https://*.js" {
    const content: unknown;
    export default content;
}

declare global {
    export type CarType = {
        id: unknown,
        latitude: number,
        longitude: number
    }

    export type StationType = {
        name: string;
        lat: number;
        lng: number;
    }

    export interface GpsCoord {
        lat: number;
        lng: number;
    }

    export type MdtLog = {
        sec: number;
        gcd: string;
        lat: number;
        lon: number;
        ang: number;
        spd: number;
        sum: number;
        bat: number;
    }
}

export { };
