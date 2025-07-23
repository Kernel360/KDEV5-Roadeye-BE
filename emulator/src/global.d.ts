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
        lon: number;
    }

    export interface GpsCoord {
        lat: number;
        lon: number;
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
