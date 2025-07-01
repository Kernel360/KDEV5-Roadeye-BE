import { useState } from "react";
import CarLogList from "./CarLogList";
import { useEmulatorContext } from "./context";

function CarInfo() {
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

export default CarInfo;