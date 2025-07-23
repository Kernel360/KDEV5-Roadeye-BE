import { k6Fetch } from '../k6utils';
import { GraphHopperRouting } from "./routing";

const instance = {
    routing: new GraphHopperRouting({
        host: import.meta.env.VITE_GRAPH_HOPPER_BASE_URL,
        key: import.meta.env.VITE_GRAPH_HOPPER_API_KEY,
        endpoint: "/route",
        httpClient: k6Fetch
    })
} as const;

export async function findRoute(profile: string, startPoint: GpsCoord, endPoint: GpsCoord) {
    return await instance.routing.doRequest({
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