export interface CommonFields {
    tid: string;
    mid: string;
    pv: string;
    did: string;
}

export interface DriveData {
    sec: string;
    gcd: string;
    lat: string;
    lon: string;
    spd: number;
    sum: number;
}

export interface IgnitionPayload {
    tid: string;
    mid: string;
    pv: string;
    did: string;
    mdn: string;
    onTime: string;
    offTime: string;
    gcd: string;
    lat: string;
    lon: string;
    sum: number;
}

export interface CycleLogPayload {
    tid: string;
    mid: string;
    pv: string;
    did: string;
    mdn: string;
    oTime: string;
    cCnt: number;
    cList: DriveData[];
}

export interface ShutdownData {
    lat: number;
    lon: number;
    sum: number;
}

export interface LogEntry {
    timestamp: string;
    message: string;
}

export interface SimulatorState {
    hostUrl: string;
    ignitionTime: string | null;
    cumulativeDistance: number;
    gpsStatus: string;
    driving: boolean;
    driveData: DriveData[];
    lastLat: number;
    lastLon: number;
    logs: LogEntry[];
    lastShutdownData: ShutdownData | null;
    globalTuid: string | null;
} 