declare module "https://*.js" {
    const content: any;
    export default content;
}

declare global {
    export type Station = {
        region: string;
        name: string;
        lat: number;
        lng: number;
    }

    export interface GpsCoord {
        lat: number;
        lng: number;
    }

    export type MdtLog = GpsCoord & {
        sec: number;
        gcd: string;
        ang: number;
        spd: number;
        sum: number;
        bat: number;
    }
}

export { };
