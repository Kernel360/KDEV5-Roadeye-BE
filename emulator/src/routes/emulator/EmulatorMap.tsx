import { MapPin } from "lucide-react"
import { useCallback, useState } from "react"
import { CustomOverlayMap, Map, MapMarker, MapTypeControl, Polyline, ZoomControl } from "react-kakao-maps-sdk"
import ArrowBox from "../../components/arrowBox"
import useKakaoMap from "../../hooks/useKakaoMap"


import MapPinBlue from "../../assets/map-pin-blue.png"
import MapPinRed from "../../assets/map-pin-red.png"
import { useEmulatorContext } from "./context"

function EmulatorMap() {
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

export default EmulatorMap;