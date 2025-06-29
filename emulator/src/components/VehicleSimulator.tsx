import React, { useCallback, useEffect, useRef } from 'react';
import { produce } from 'immer';
import type { CycleLogPayload, DriveData, IgnitionPayload, SimulatorState } from '../types/vehicle';
import { COMMON_FIELDS, createLogEntry, formatTime, getDistance, VEHICLE_ID } from '../utils/vehicleUtils';
import { sendCycleLog, sendIgnitionOff, sendIgnitionOn } from '../lib/api';
import './VehicleSimulator.css';

const VehicleSimulator: React.FC = () => {
    const [state, setState] = React.useState<SimulatorState>({
        hostUrl: 'http://localhost:8080',
        ignitionTime: null,
        cumulativeDistance: 0,
        gpsStatus: 'A',
        driving: false,
        driveData: [],
        lastLat: 37.5665,
        lastLon: 126.9780,
        logs: [],
        lastShutdownData: null,
        globalTuid: null
    });

    const drivingIntervalRef = useRef<NodeJS.Timeout | null>(null);

    const addLog = useCallback((message: string) => {
        const logEntry = createLogEntry(message);
        setState(produce(draft => {
            draft.logs.push(logEntry);
        }));
    }, []);

    const handleIgnitionOn = useCallback(async () => {
        const now = new Date();
        const onTime = formatTime(now);

        if (state.ignitionTime) {
            addLog("⚠️ 이미 시동이 켜져 있습니다.");
            return;
        }

        const newTuid = crypto.randomUUID();

        let newCumulativeDistance = state.cumulativeDistance;

        if (state.lastShutdownData) {
            const distanceFromLast = getDistance(
                state.lastShutdownData.lat,
                state.lastShutdownData.lon,
                state.lastLat,
                state.lastLon
            );

            if (distanceFromLast < 80) {
                newCumulativeDistance = state.lastShutdownData.sum + distanceFromLast;
            } else {
                addLog(`🟡 시동 ON 거리 (${Math.round(distanceFromLast)}m)가 80m 초과로 무시됨`);
                newCumulativeDistance = state.lastShutdownData.sum;
            }
        }

        setState(produce(draft => {
            draft.ignitionTime = onTime;
            draft.cumulativeDistance = newCumulativeDistance;
            draft.globalTuid = newTuid;
        }));

        const payload: IgnitionPayload = {
            ...COMMON_FIELDS,
            mdn: VEHICLE_ID,
            onTime: onTime,
            offTime: '',
            gcd: state.gpsStatus,
            lat: state.lastLat.toFixed(6),
            lon: state.lastLon.toFixed(6),
            sum: Math.floor(newCumulativeDistance)
        };

        const result = await sendIgnitionOn(state.hostUrl, payload, newTuid);
        addLog(`POST to ${state.hostUrl}/api/ignition/on:\n${JSON.stringify(payload, null, 2)}`);
        addLog(`응답 상태: ${result.status}`);
        addLog("🔑 시동 ON");
    }, [state.ignitionTime, state.lastShutdownData, state.lastLat, state.lastLon, state.cumulativeDistance, state.gpsStatus, state.hostUrl, addLog]);

    const handleIgnitionOff = useCallback(async () => {
        const now = new Date();
        const offTime = formatTime(now);

        if (!state.ignitionTime) {
            addLog("⚠️ 시동이 꺼져 있습니다. OFF 요청 불가.");
            return;
        }

        if (state.driving) {
            if (drivingIntervalRef.current) {
                clearInterval(drivingIntervalRef.current);
                drivingIntervalRef.current = null;
            }

            setState(produce(draft => {
                draft.driving = false;
            }));

            if (state.driveData.length > 0) {
                const body: CycleLogPayload = {
                    ...COMMON_FIELDS,
                    mdn: VEHICLE_ID,
                    oTime: formatTime(now).slice(0, 12),
                    cCnt: state.driveData.length,
                    cList: state.driveData
                };
                const result = await sendCycleLog(state.hostUrl, body, state.globalTuid!);
                addLog(`POST to ${state.hostUrl}/api/cycle-log:\n${JSON.stringify(body, null, 2)}`);
                addLog(`응답 상태: ${result.status}`);

                setState(produce(draft => {
                    draft.driveData = [];
                }));
            }

            setState(produce(draft => {
                draft.lastShutdownData = {
                    lat: state.lastLat,
                    lon: state.lastLon,
                    sum: state.cumulativeDistance
                };
            }));

            addLog("주행 종료 (시동 OFF로 종료)");
        }

        const payload: IgnitionPayload = {
            ...COMMON_FIELDS,
            mdn: VEHICLE_ID,
            onTime: state.ignitionTime,
            offTime: offTime,
            gcd: state.gpsStatus,
            lat: state.lastLat.toFixed(6),
            lon: state.lastLon.toFixed(6),
            sum: Math.floor(state.cumulativeDistance)
        };

        const result = await sendIgnitionOff(state.hostUrl, payload, state.globalTuid!);
        addLog(`POST to ${state.hostUrl}/api/ignition/off:\n${JSON.stringify(payload, null, 2)}`);
        addLog(`응답 상태: ${result.status}`);

        setState(produce(draft => {
            draft.ignitionTime = null;
            draft.globalTuid = null;
        }));

        addLog("🛑 시동 OFF");
    }, [state.ignitionTime, state.driving, state.driveData, state.lastLat, state.lastLon, state.cumulativeDistance, state.gpsStatus, state.hostUrl, state.globalTuid, addLog]);

    const startDriving = useCallback(() => {
        if (!state.ignitionTime) {
            addLog("⚠️ 시동이 먼저 켜져야 합니다.");
            return;
        }

        if (state.driving) {
            addLog("⚠️ 이미 주행 중입니다.");
            return;
        }

        setState(produce(draft => {
            draft.driving = true;
            draft.driveData = [];
        }));

        addLog("🚗 주행 시작");

        drivingIntervalRef.current = setInterval(() => {
            const now = new Date();
            const sec = now.getSeconds().toString().padStart(2, '0');

            setState(produce(draft => {
                draft.lastLat += 0.0001;
                draft.lastLon += 0.0001;

                const spd = Math.floor(Math.random() * 20) + 30;
                const distance = (spd * 1000 / 3600);
                draft.cumulativeDistance += distance;

                const newDriveData: DriveData = {
                    sec: sec,
                    gcd: draft.gpsStatus,
                    lat: draft.lastLat.toFixed(6),
                    lon: draft.lastLon.toFixed(6),
                    spd: spd,
                    sum: Math.floor(draft.cumulativeDistance)
                };

                draft.driveData.push(newDriveData);

                if (draft.driveData.length === 60) {
                    const body: CycleLogPayload = {
                        ...COMMON_FIELDS,
                        mdn: VEHICLE_ID,
                        oTime: formatTime(now).slice(0, 12),
                        cCnt: 60,
                        cList: [...draft.driveData]
                    };

                    sendCycleLog(draft.hostUrl, body, draft.globalTuid!).then(result => {
                        addLog(`POST to ${draft.hostUrl}/api/cycle-log:\n${JSON.stringify(body, null, 2)}`);
                        addLog(`응답 상태: ${result.status}`);
                    });

                    draft.driveData = [];
                }
            }));
        }, 1000);
    }, [state.ignitionTime, state.driving, state.gpsStatus, state.hostUrl, state.globalTuid, addLog]);

    useEffect(() => {
        return () => {
            if (drivingIntervalRef.current) {
                clearInterval(drivingIntervalRef.current);
            }
        };
    }, []);

    return (
        <div className="vehicle-simulator">
            <h1>차량 데이터 시뮬레이터</h1>

            <div className="input-group">
                <label htmlFor="host-url">API 서버 주소:</label>
                <input
                    type="text"
                    id="host-url"
                    value={state.hostUrl}
                    onChange={(e) => setState(produce(draft => {
                        draft.hostUrl = e.target.value;
                    }))}
                    className="host-input"
                />
            </div>

            <div className="button-group">
                <button
                    onClick={handleIgnitionOn}
                    disabled={state.ignitionTime !== null}
                    className="btn btn-ignition-on"
                >
                    시동 ON
                </button>
                <button
                    onClick={startDriving}
                    disabled={state.ignitionTime === null || state.driving}
                    className="btn btn-drive"
                >
                    주행 시작
                </button>
                <button
                    onClick={handleIgnitionOff}
                    disabled={state.ignitionTime === null}
                    className="btn btn-ignition-off"
                >
                    시동 OFF
                </button>
            </div>

            <div className="log-container">
                <textarea
                    value={state.logs.map(log => `[${log.timestamp}] ${log.message}`).join('\n')}
                    readOnly
                    className="log-area"
                    placeholder="로그가 여기에 표시됩니다..."
                />
            </div>
        </div>
    );
};

export default VehicleSimulator;
