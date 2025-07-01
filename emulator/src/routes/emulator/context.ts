import { createContext, useContext } from "react";

export type EmulatorContextType = {
    state: EmulatorContextState;
    actions: EmulatorContextAction;
}
export type EmulatorContextState = {
    mapCenter: GpsCoord
    startPoint: GpsCoord | null;
    currentPoint: GpsCoord | null;
    endPoint: GpsCoord | null;
    pathRoute: GpsCoord[]
}
export type EmulatorContextAction = {
    setStartPoint: (point: GpsCoord) => void;
    setEndPoint: (point: GpsCoord) => void;
    setMapCenter: (point: GpsCoord) => void;
    setPathRoute: (route: GpsCoord[]) => void;
}

function createDefaultContext(): EmulatorContextType {
    return {
        state: {
            mapCenter: {
                lat: 37.499225,
                lng: 127.031477
            },
            startPoint: null,
            currentPoint: null,
            endPoint: null,
            pathRoute: []
        },
        actions: {
            setStartPoint: () => { },
            setEndPoint: () => { },
            setMapCenter: () => { },
            setPathRoute: () => { }
        }
    }
}

const EmulatorContext = createContext<EmulatorContextType>(createDefaultContext());

export const EmulatorContextProvider = EmulatorContext.Provider;
export const useEmulatorContext = () => useContext(EmulatorContext);