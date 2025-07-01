import { MapPin } from "lucide-react"
import { useCallback, useState, useEffect } from "react"
import { CustomOverlayMap, Map, MapMarker, MapTypeControl, Polyline, ZoomControl } from "react-kakao-maps-sdk"
import ArrowBox from "~/components/arrowBox"
import useKakaoMap from "~/hooks/useKakaoMap"
import MapPinBlue from "~/assets/map-pin-blue.png"
import MapPinRed from "~/assets/map-pin-red.png"
import CarMarker from "~/assets/car-marker.svg"
import { useEmulatorStore, type GpsCoord } from "~/stores/emulatorStore"

function EmulatorMap() {
    useKakaoMap();

    const {
        mapCenter,
        startPoint,
        endPoint,
        pathRoute,
        selectedCar,
        setStartPoint,
        setEndPoint,
        setMapCenter
    } = useEmulatorStore();

    const [contextMenuCoord, setContextMenuCoord] = useState<GpsCoord | null>(null);

    // 선택된 차량이 변경되면 자동으로 지도 중심을 차량 위치로 이동
    useEffect(() => {
        if (selectedCar) {
            setMapCenter({
                lat: selectedCar.latitude,
                lng: selectedCar.longitude
            });
        }
    }, [selectedCar, setMapCenter]);

    const handleRightClick = useCallback((_: unknown, MouseEvent: kakao.maps.event.MouseEvent) => {
        setContextMenuCoord({
            lat: MouseEvent.latLng.getLat(),
            lng: MouseEvent.latLng.getLng()
        })
    }, [])

    const handleCenterChanged = useCallback((map: kakao.maps.Map) => {
        setMapCenter({
            lat: map.getCenter().getLat(),
            lng: map.getCenter().getLng()
        })
    }, [setMapCenter])

    return (
        <Map
            style={{ width: '100%', height: '100%' }}
            level={3}
            center={mapCenter}
            onRightClick={handleRightClick}
            onCenterChanged={handleCenterChanged}
        >
            <MapTypeControl position={"TOPRIGHT"} />
            <ZoomControl position={"BOTTOMRIGHT"} />

            {startPoint && <MapMarker
                image={{
                    src: MapPinBlue,
                    size: { width: 31.2, height: 40 }
                }}
                position={startPoint}
            />}
            {endPoint && <MapMarker
                image={{
                    src: MapPinRed,
                    size: { width: 31.2, height: 40 }
                }}
                position={endPoint}
            />}

            {selectedCar && (
                <MapMarker
                    image={{
                        src: CarMarker,
                        size: { width: 32, height: 32 }
                    }}
                    position={{
                        lat: selectedCar.latitude,
                        lng: selectedCar.longitude
                    }}
                />
            )}

            {contextMenuCoord && (
                <CustomOverlayMap
                    position={contextMenuCoord}
                    yAnchor={1.3}
                >
                    <ArrowBox width="130px" height="80px">
                        <div className="flex flex-col gap-2 select-none">
                            <div className="flex flex-row cursor-pointer" onClick={() => {
                                setStartPoint(contextMenuCoord)
                                setContextMenuCoord(null)
                            }}>
                                <MapPin color="blue" />
                                <div>출발지 설정</div>
                            </div>
                            <div className="flex flex-row cursor-pointer" onClick={() => {
                                setEndPoint(contextMenuCoord)
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

            {pathRoute && (
                <Polyline
                    path={pathRoute}
                    strokeWeight={5}
                    endArrow={true}
                />
            )}
        </Map>
    )
}

export default EmulatorMap;