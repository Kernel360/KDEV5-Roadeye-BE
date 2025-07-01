declare module "https://*.js" {
    const content: any;
    export default content;
}

declare global {
    export type Station = {
        region: string;
        name: string;
        lat: number;
        lon: number;
    }

    export type GpsLocation = {
        lat: number;
        lon: number;
    }

    export type MdtLog = GpsLocation & {
        sec: number;
        gcd: string;
        ang: number;
        spd: number;
        sum: number;
        bat: number;
    }
}

export {};
