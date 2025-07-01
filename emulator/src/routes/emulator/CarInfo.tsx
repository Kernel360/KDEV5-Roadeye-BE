import { useCallback } from "react";
import { useEmulatorStore, useSelectedEmulatorCar, useSelectedEmulatorDriving, useSelectedEmulatorEndPoint, useSelectedEmulatorIgnition, useSelectedEmulatorStartPoint } from "~/stores/emulatorStore";
import CarLogList from "./CarLogList";

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
    const { setMapCenter } = useEmulatorStore();

    const startPoint = useSelectedEmulatorStartPoint();
    const endPoint = useSelectedEmulatorEndPoint();

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
    const car = useSelectedEmulatorCar();
    const { centerOnSelectedCar } = useEmulatorStore();

    return (
        <div>
            <div>차량 상세 정보</div>
            <div className="mt-2 space-y-2">
                <div className="flex justify-between">
                    <span className="text-sm text-gray-600">ID:</span>
                    <span className="text-sm font-medium">
                        {car ? (car.id) : '-'}
                    </span>
                </div>
                <div className="flex justify-between">
                    <span className="text-sm text-gray-600">차량명:</span>
                    <span className="text-sm font-medium">
                        {car ? (car.name) : '-'}
                    </span>
                </div>
                <div className="flex justify-between">
                    <span className="text-sm text-gray-600">시동:</span>
                    <span className="text-sm font-medium">
                        {car ? (car.ignitionStatus) : '-'}
                    </span>
                </div>
                <div className="flex justify-between">
                    <span className="text-sm text-gray-600">위도:</span>
                    <span className="text-sm font-medium">
                        {car ? car.latitude.toFixed(6) : '-'}
                    </span>
                </div>
                <div className="flex justify-between">
                    <span className="text-sm text-gray-600">경도:</span>
                    <span className="text-sm font-medium">
                        {car ? car.longitude.toFixed(6) : '-'}
                    </span>
                </div>
                <div className="mt-3">
                    <button
                        onClick={centerOnSelectedCar}
                        disabled={!car}
                        className={`w-full px-3 py-2 text-sm rounded transition-colors 
                            ${!car
                                ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                                : 'bg-blue-500 text-white hover:bg-blue-600'
                            }`
                        }
                    >
                        차량 위치로 이동
                    </button>
                </div>
            </div>
        </div>
    )
}

type IgnitionState = '시동OFF' | '시동ON';
type DrivingState = '주행' | '정지';

const ignitionStateGraph: Record<IgnitionState, IgnitionState> = {
    '시동OFF': '시동ON',
    '시동ON': '시동OFF',
};

const drivingStateGraph: Record<DrivingState, DrivingState> = {
    '주행': '정지',
    '정지': '주행',
}

function CarControl() {
    const { selectedCar, turnOffIgnition, turnOnIgnition } = useEmulatorStore();

    const ignition = useSelectedEmulatorIgnition();
    const driving = useSelectedEmulatorDriving();

    const handleIgnitionStateChange = useCallback(() => {
        if (!selectedCar) return;


    }, [selectedCar]);

    const handleDrivingStateChange = useCallback(() => {
        if (!selectedCar) return;

        if (driving!.state === '주행') {
            turnOnIgnition();
        } else {
            turnOffIgnition();
        }

    }, [selectedCar, driving, turnOnIgnition, turnOffIgnition]);

    const ignitionButtonDisabled = !selectedCar || (ignition!.state === '시동ON' && driving!.state === '주행');
    const drivingButtonDisabled = !selectedCar || (ignition!.state === '시동OFF' && driving!.state === '정지');

    return (
        <div className="flex flex-row gap-2">
            <button
                onClick={handleIgnitionStateChange}
                disabled={ignitionButtonDisabled}
                className={`flex-1 px-3 py-2 text-sm rounded transition-colors 
                    ${ignitionButtonDisabled
                        ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                        : 'bg-blue-500 text-white hover:bg-blue-600'
                    }`
                }
            >
                {selectedCar ? ignitionStateGraph[ignition!.state] : '시동 ON'}
            </button>
            <button
                onClick={handleDrivingStateChange}
                disabled={drivingButtonDisabled}
                className={`flex-1 px-3 py-2 text-sm rounded transition-colors 
                    ${drivingButtonDisabled
                        ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                        : 'bg-red-500 text-white hover:bg-red-600'
                    }`
                }
            >
                {selectedCar ? drivingStateGraph[driving!.state] : '정지'}
            </button>
        </div>
    )
}


export default CarInfo;