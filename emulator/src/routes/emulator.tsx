import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react"
import useKakaoMap from "../hooks/useKakaoMap"
import { CustomOverlayMap, Map, MapMarker, MapTypeControl, Polyline, ZoomControl } from "react-kakao-maps-sdk"
import ArrowBox from "../components/arrowBox"
import { MapPin } from "lucide-react"
import { useImmer } from "use-immer"

import MapPinRed from "../assets/map-pin-red.png"
import MapPinBlue from "../assets/map-pin-blue.png"
import { findRoute } from "../lib/graphhopper"

type EmulatorContextType = {
    state: EmulatorContextState;
    actions: EmulatorContextAction;
}
type EmulatorContextState = {
    mapCenter: GpsCoord
    startPoint: GpsCoord | null;
    currentPoint: GpsCoord | null;
    endPoint: GpsCoord | null;
    pathRoute: GpsCoord[]
}
type EmulatorContextAction = {
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
const useEmulatorContext = () => useContext(EmulatorContext);

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
        <EmulatorContext.Provider value={context}>
            <div className="flex w-screen h-screen gap-2 p-2">
                <SideBar />
                <div className="flex-1 flex flex-row gap-2">
                    <CarView />
                    <MapView />
                </div>
            </div>
            <RouteAPI />
        </EmulatorContext.Provider>
    )

}

function SideBar() {
    return <div className="min-w-40">Side Bar</div>
}

function CarView() {
    return (
        <div className="flex flex-col gap-2 min-w-60">
            <CarStatus />
            <CarControl />
            <CarLogList />
        </div>
    )
}

function CarStatus() {
    const { state, actions } = useEmulatorContext();

    return (
        <div>
            <div>차량 상태</div>
            <div>
                <div className="flex flex-row gap-2">
                    <span className="select-none">출발지: </span>
                    <span>{state.startPoint ? `${state.startPoint.lat.toFixed(4)}, ${state.startPoint.lng.toFixed(4)}` : '지정안됨'}</span>
                    <div
                        className="cursor-pointer select-none"
                        onClick={() => {
                            if (state.startPoint) {
                                actions.setMapCenter(state.startPoint)
                            }
                        }}>
                        이동
                    </div>
                </div>
                <div className="flex flex-row gap-2">
                    <span className="select-none">도착지: </span>
                    <span>{state.endPoint ? `${state.endPoint.lat.toFixed(4)}, ${state.endPoint.lng.toFixed(4)}` : '지정안됨'}</span>
                    <div
                        className="cursor-pointer select-none"
                        onClick={() => {
                            if (state.endPoint) {
                                actions.setMapCenter(state.endPoint)
                            }
                        }}>
                        이동
                    </div>
                </div>
            </div>
        </div>
    )
}

const carStatusGraph = {
    '시동OFF': ['시동ON'] as const,
    '시동ON': ['주행', '시동OFF'] as const,
    '주행': ['정지'] as const,
    '정지': ['주행', '시동OFF'] as const
} as const

function CarControl() {
    const [state, setState] = useState<keyof typeof carStatusGraph>('시동OFF');

    const graph = carStatusGraph[state as keyof typeof carStatusGraph];

    return (
        <div>
            <div className="flex flex-row gap-2">
                {graph.map((state) => (
                    <button key={state} onClick={() => setState(state)}>{state}</button>
                ))}
            </div>
        </div>
    )
}

function CarLogList() {
    return (
        <div>
            <ul>
                <li>Car Log 1</li>
                <li>Car Log 2</li>
                <li>Car Log 3</li>
            </ul>
        </div>
    )
}

function MapView() {
    useKakaoMap();

    const { state, actions } = useEmulatorContext();

    const [contextMenuCoord, setContextMenuCoord] = useState<GpsCoord | null>(null);

    const handleRightClick = useCallback((_: unknown, MouseEvent: kakao.maps.event.MouseEvent) => {
        setContextMenuCoord({
            lat: MouseEvent.latLng.getLat(),
            lng: MouseEvent.latLng.getLng()
        })
    }, [])

    return (
        <Map
            style={{ width: '100%', height: '100%' }}
            level={3}
            center={state.mapCenter}
            onRightClick={handleRightClick}
        >
            <MapTypeControl position={"TOPRIGHT"} />
            <ZoomControl position={"BOTTOMRIGHT"} />

            {state.startPoint && <MapMarker
                image={{
                    src: MapPinBlue,
                    size: { width: 31.2, height: 40 }
                }}
                position={state.startPoint}
            />}
            {state.endPoint && <MapMarker
                image={{
                    src: MapPinRed,
                    size: { width: 31.2, height: 40 }
                }}
                position={state.endPoint}
            />}

            {contextMenuCoord && (
                <CustomOverlayMap
                    position={contextMenuCoord}
                    yAnchor={1.3}
                >
                    <ArrowBox width="130px" height="80px">
                        <div className="flex flex-col gap-2 select-none">
                            <div className="flex flex-row cursor-pointer" onClick={() => {
                                actions.setStartPoint(contextMenuCoord)
                                setContextMenuCoord(null)
                            }}>
                                <MapPin color="blue" />
                                <div>출발지 설정</div>
                            </div>
                            <div className="flex flex-row cursor-pointer" onClick={() => {
                                actions.setEndPoint(contextMenuCoord)
                                setContextMenuCoord(null)
                            }}>
                                <MapPin color="red" />
                                <div className="inline-block w-fit">
                                    도착지 설정
                                </div>
                            </div>
                        </div>
                    </ArrowBox>
                </CustomOverlayMap>
            )}

            {state.pathRoute && (
                <Polyline
                    path={state.pathRoute}
                    strokeWeight={5}
                    endArrow={true}
                />
            )}
        </Map>
    )
}

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

export default Emulator;