import { useEffect } from "react";
import { findRoute } from "../../lib/graphhopper";
import { useEmulatorContext } from "./context";

function RouteAPI() {
    const { state, actions } = useEmulatorContext();

    useEffect(() => {
        if (state.startPoint && state.endPoint) {
            findRoute("car", state.startPoint, state.endPoint)
                .then((res) => {
                    actions.setPathRoute(res.paths[0].points.coordinates)
                })
                .catch((err) => {
                    console.error(err)
                })
        }
    }, [state.startPoint, state.endPoint, actions])

    return <></>
}

export default RouteAPI;