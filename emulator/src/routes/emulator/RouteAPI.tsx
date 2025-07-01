import { useEffect } from "react";
import { findRoute } from "~/lib/graphhopper";
import { useEmulatorStore } from "~/stores/emulatorStore";

function RouteAPI() {
    const { selectedCar, setPathRoute } = useEmulatorStore();

    useEffect(() => {
        if (selectedCar?.emulator.startPoint && selectedCar?.emulator.endPoint) {
            findRoute("car", selectedCar.emulator.startPoint, selectedCar.emulator.endPoint)
                .then((res) => {
                    setPathRoute(res.paths[0].points.coordinates)
                })
                .catch((err) => {
                    console.error(err)
                })
        }
    }, [selectedCar?.emulator.startPoint, selectedCar?.emulator.endPoint, setPathRoute])

    return <></>
}

export default RouteAPI;