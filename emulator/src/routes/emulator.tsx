import { useMemo } from "react"
import { useImmer } from "use-immer"

import CarInfo from "./emulator/CarInfo"
import type { EmulatorContextAction, EmulatorContextState, EmulatorContextType } from "./emulator/context"
import { EmulatorContextProvider } from "./emulator/context"
import EmulatorMap from "./emulator/EmulatorMap"
import SideBar from "./emulator/Sidebar"
import RouteAPI from "./emulator/RouteAPI"

function Emulator() {
    const [state, updateState] = useImmer<EmulatorContextState>({
        mapCenter: {
            lat: 37.499225,
            lng: 127.031477
        },
        startPoint: null,
        currentPoint: null,
        endPoint: null,
        pathRoute: []
    })
    const actions = useMemo<EmulatorContextAction>(() => {
        return {
            setStartPoint: (point: GpsCoord) => updateState((draft) => {
                draft.startPoint = point
            }),
            setEndPoint: (point: GpsCoord) => updateState((draft) => {
                draft.endPoint = point
            }),
            setMapCenter: (point: GpsCoord) => updateState((draft) => {
                draft.mapCenter = point
            }),
            setPathRoute: (route: GpsCoord[]) => updateState((draft) => {
                draft.pathRoute = route
            })
        }
    }, [updateState])

    const context: EmulatorContextType = {
        state,
        actions
    }

    return (
        <EmulatorContextProvider value={context}>
            <div className="flex w-screen h-screen gap-2 p-2">
                <SideBar />
                <div className="flex-1 flex flex-row gap-2">
                    <CarInfo />
                    <EmulatorMap />
                </div>
            </div>
            <RouteAPI />
        </EmulatorContextProvider>
    )

}

export default Emulator;