import { GraphHopperRouting } from "./routing";

const BASE_URL = import.meta.env.VITE_GRAPH_HOPPER_BASE_URL;

const instance = {
    routing: new GraphHopperRouting({
        key: import.meta.env.VITE_GRAPH_HOPPER_API_KEY || "",
        host: BASE_URL,
        endpoint: "/route"
    })
} as const;

export function findRoute(profile: string, startPoint: GpsCoord, endPoint: GpsCoord) {
    return instance.routing.doRequest({
        profile,
        points: [
            startPoint,
            endPoint
        ],
        "elevation": false,
        "instructions": false,
        "locale": "ko",
        "points_encoded": false,
        "details": [
            "road_class",
            "road_environment",
            "max_speed",
            "average_speed"
        ],
        "snap_preventions": ["ferry"]
    });
}