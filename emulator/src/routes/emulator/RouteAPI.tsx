import { useEffect } from "react";
import { emulateCarPath } from "~/lib/emulator";
import { findRoute } from "~/lib/graphhopper";
import { useEmulatorStore, useSelectedEmulatorEndPoint, useSelectedEmulatorStartPoint } from "~/stores/emulatorStore";

function RouteAPI() {
    const { setCarPathRoute, setCarEmulatorInstance } = useEmulatorStore();

    const startPoint = useSelectedEmulatorStartPoint();
    const endPoint = useSelectedEmulatorEndPoint();

    useEffect(() => {
        if (startPoint && endPoint) {
            findRoute("car", startPoint, endPoint)
                .then((res) => {
                    setCarPathRoute(res.paths[0].points.coordinates)
                    return emulateCarPath({
                        start: startPoint,
                        end: endPoint,
                        initSpdKmh: 10,
                        maxSpdKmh: 80,
                        initMileage: 0,
                        acc: 0.5
                    })
                })
                .then((instance) => {
                    setCarEmulatorInstance(instance)
                })
        }
    }, [startPoint, endPoint, setCarPathRoute, setCarEmulatorInstance])

    return <></>
}

export default RouteAPI;