// https://github.com/graphhopper/directions-api-js-client/blob/master/src/GraphHopperRouting.js

import { decodePath, extractError } from './ghutils';

export type GraphHopperRequestArgs = {
    profile: string;
    points: GpsCoord[];
} & Partial<{
    elevation: boolean;
    instructions: boolean;
    locale: string;
    points_encoded: boolean;
    points_encoded_multiplier: number;
    details: string[];
    snap_preventions: string[];
}>

export type GraphHopperRoutingDefaultConfig = {
    profile?: string;
    debug?: boolean;
    locale?: string;
    points_encoded?: boolean;
    instructions?: boolean;
    elevation?: boolean;
    optimize?: "false" | "true";
}

interface GraphHopperRawResponse {
    paths?: Path[];
}

interface GraphHopperResponse {
    paths: DecodedPath[];
}

export interface LineString {
    type: "LineString";
    coordinates: GpsCoord[];
}

export interface Path {
    points_encoded?: boolean;
    points: string | LineString;
    snapped_waypoints: LineString;
    instructions?: Instruction[];
}

export interface DecodedPath {
    points_encoded?: boolean;
    points: LineString;
    snapped_waypoints: LineString;
    instructions?: Instruction[];
}

export interface Instruction {
    interval: number[];
    points?: number[][];
    [key: string]: any;
}

export interface GraphHopperArgs {
    key: string;
    host?: string;
    endpoint?: string;
    timeout?: number;
    turn_sign_map?: { [key: string]: string };
    httpClient?: (url: string, init: {
        method: string,
        body?: string,
        headers?: {
            [key: string]: string;
        }
    }) => Promise<Response>;
}

export class GraphHopperRouting {
    private host: string;
    private endpoint: string;
    private key: string;
    private timeout: number;
    public defaults: GraphHopperRoutingDefaultConfig;
    public turn_sign_map: { [key: string]: string };
    private httpClient: GraphHopperArgs['httpClient'];

    constructor(args: GraphHopperArgs, requestDefaults?: GraphHopperRoutingDefaultConfig) {
        this.defaults = {
            profile: "car",
            debug: false,
            locale: "en",
            points_encoded: true,
            instructions: true,
            elevation: true,
            optimize: "false"
        };

        if (requestDefaults) {
            Object.keys(requestDefaults).forEach(key => {
                // @ts-ignore
                this.defaults[key] = requestDefaults[key];
            });
        }

        this.key = args.key;
        this.host = args.host ? args.host : "https://graphhopper.com/api/1";
        this.endpoint = args.endpoint ? args.endpoint : '/route';
        this.timeout = args.timeout ? args.timeout : 10000;
        this.turn_sign_map = args.turn_sign_map ? args.turn_sign_map : {
            "-6": "leave roundabout",
            "-3": "turn sharp left",
            "-2": "turn left",
            "-1": "turn slight left",
            0: "continue",
            1: "turn slight right",
            2: "turn right",
            3: "turn sharp right",
            4: "finish",
            5: "reached via point",
            6: "enter roundabout"
        };
        this.httpClient = args.httpClient || ((url, init) => fetch(url, init));
    }

    private pointArrToObj(points: number[][]): GpsCoord[] {
        return points.map(p => ({ lat: p[1], lng: p[0] }));
    }

    async doRequest(reqArgs: GraphHopperRequestArgs): Promise<GraphHopperResponse> {
        Object.keys(this.defaults).forEach(key => {
            if (reqArgs[key as keyof GraphHopperRequestArgs]) {
                // @ts-ignore
                reqArgs[key] = this.defaults[key];
            }
        });

        const url = this.host + this.endpoint + "?key=" + this.key;

        try {
            const response = await this.httpClient!(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    ...reqArgs,
                    points: reqArgs.points.map(p => [p.lng, p.lat])
                })
            });

            if (!response.ok) {
                throw extractError({ status: response.status, data: await response.text() }, url);
            }

            const data: GraphHopperRawResponse = await response.json();

            if (data.paths) {
                for (let i = 0; i < data.paths.length; i++) {
                    const path = data.paths[i];

                    if (path.points_encoded) {
                        // @ts-ignore
                        const tmpArray = decodePath(path.points as string, reqArgs.elevation);
                        path.points = {
                            "type": "LineString",
                            // @ts-ignore
                            "coordinates": tmpArray
                        };

                        // @ts-ignore
                        const tmpSnappedArray = decodePath(path.snapped_waypoints as string, reqArgs.elevation);
                        path.snapped_waypoints = {
                            "type": "LineString",
                            // @ts-ignore
                            "coordinates": tmpSnappedArray
                        };
                    }

                    // @ts-ignore
                    path.points.coordinates = this.pointArrToObj(path.points.coordinates as number[][]);

                    // @ts-ignore
                    path.snapped_waypoints.coordinates = this.pointArrToObj(path.snapped_waypoints.coordinates as number[][]);


                    if (path.instructions) {
                        for (let j = 0; j < path.instructions.length; j++) {
                            // Add a LngLat to every instruction
                            const interval = path.instructions[j].interval;
                            const coordinates = (path.points as LineString).coordinates;
                            // The second parameter of slice is non inclusive, therefore we have to add +1
                            path.instructions[j].points = coordinates.slice(interval[0], interval[1] + 1).map(p => [p.lng, p.lat]);
                        }
                    }
                }
            }

            return data as GraphHopperResponse;
        } catch (error: any) {
            throw extractError(error, url);
        }
    }
}