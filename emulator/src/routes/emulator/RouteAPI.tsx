import { useEffect } from "react";
import { findRoute } from "../../lib/graphhopper";
import { useEmulatorStore } from "../../stores/emulatorStore";

function RouteAPI() {
    const { startPoint, endPoint, setPathRoute } = useEmulatorStore();

    useEffect(() => {
        if (startPoint && endPoint) {
            findRoute("car", startPoint, endPoint)
                .then((res) => {
                    setPathRoute(res.paths[0].points.coordinates)
                })
                .catch((err) => {
                    console.error(err)
                })
        }
    }, [startPoint, endPoint, setPathRoute])

    return <></>
}

export default RouteAPI;