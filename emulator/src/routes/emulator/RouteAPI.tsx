import { useEffect } from "react";
import { useEmulatorStore, useSelectedEmulatorEndPoint, useSelectedEmulatorStartPoint } from "~/stores/emulatorStore";

function RouteAPI() {
    const { findPathRoute } = useEmulatorStore();

    const startPoint = useSelectedEmulatorStartPoint();
    const endPoint = useSelectedEmulatorEndPoint();

    useEffect(() => {
        if (startPoint && endPoint) {
            findPathRoute(startPoint, endPoint)
        }
    }, [startPoint, endPoint, findPathRoute])

    return <></>
}

export default RouteAPI;