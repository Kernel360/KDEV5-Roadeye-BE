export interface CommonFields {
    mdn: string;
    tid: string;
    mid: string;
    pv: number;
    did: string;
}

export type DriveData = {
    sec: number;
    gcd: string;
    lat: number;
    lon: number;
    ang: number;
    spd: number;
    sum: number;
    bat: number;
}

export type IgnitionOnPayload = CommonFields & {
    onTime: string;
    gcd: string;
    lat: number;
    lon: number;
    ang: number;
    spd: number;
    sum: number;
}

export type IgnitionOffPayload = CommonFields & {
    onTime: string;
    offTime: string;
    gcd: string;
    lat: number;
    lon: number;
    ang: number;
    spd: number;
    sum: number;
}

export type CycleLogPayload = CommonFields & {
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