import { useState } from "react";
import CarLogList from "./CarLogList";
import { useEmulatorStore } from "~/stores/emulatorStore";

function CarInfo() {
    return (
        <div className="flex flex-col gap-2 min-w-60">
            <CarStatus />
            <CarDetails />
            <CarControl />
            <CarLogList />
        </div>
    )
}

function CarStatus() {
    const { startPoint, endPoint, setMapCenter } = useEmulatorStore();

    return (
        <div>
            <div>차량 상태</div>
            <div>
                <div className="flex flex-row gap-2">
                    <span className="select-none">출발지: </span>
                    <span>{startPoint ? `${startPoint.lat.toFixed(4)}, ${startPoint.lng.toFixed(4)}` : '지정안됨'}</span>
                    <div
                        className="cursor-pointer select-none"
                        onClick={() => {
                            if (startPoint) {
                                setMapCenter(startPoint)
                            }
                        }}>
                        이동
                    </div>
                </div>
                <div className="flex flex-row gap-2">
                    <span className="select-none">도착지: </span>
                    <span>{endPoint ? `${endPoint.lat.toFixed(4)}, ${endPoint.lng.toFixed(4)}` : '지정안됨'}</span>
                    <div
                        className="cursor-pointer select-none"
                        onClick={() => {
                            if (endPoint) {
                                setMapCenter(endPoint)
                            }
                        }}>
                        이동
                    </div>
                </div>
            </div>
        </div>
    )
}

function CarDetails() {
    const { selectedCar, centerOnSelectedCar } = useEmulatorStore();

    return (
        <div>
            <div>차량 상세 정보</div>
            <div className="mt-2 space-y-2">
                <div className="flex justify-between">
                    <span className="text-sm text-gray-600">차량명:</span>
                    <span className="text-sm font-medium">
                        {selectedCar ? (selectedCar.name || `차량 ${selectedCar.id}`) : '-'}
                    </span>
                </div>
                <div className="flex justify-between">
                    <span className="text-sm text-gray-600">위도:</span>
                    <span className="text-sm font-medium">
                        {selectedCar ? selectedCar.latitude.toFixed(6) : '-'}
                    </span>
                </div>
                <div className="flex justify-between">
                    <span className="text-sm text-gray-600">경도:</span>
                    <span className="text-sm font-medium">
                        {selectedCar ? selectedCar.longitude.toFixed(6) : '-'}
                    </span>
                </div>
                {selectedCar && (
                    <div className="mt-3">
                        <button
                            onClick={centerOnSelectedCar}
                            className="w-full px-3 py-2 text-sm bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors"
                        >
                            차량 위치로 이동
                        </button>
                    </div>
                )}
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

export default CarInfo;