import type {CycleLogPayload, IgnitionPayload} from '../types/vehicle';
import {generateTuid, getFormattedTime} from '../utils/vehicleUtils';

export async function sendRequest(
    baseUrl: string,
    endpoint: string,
    body: IgnitionPayload | CycleLogPayload,
    tuid: string | null
): Promise<{ status: number; success: boolean }> {
    const url = `${baseUrl}${endpoint}`;

    const headers = {
        "Content-Type": "application/json; charset=utf-8",
        "Accept": "application/json; charset=utf-8",
        "Cache-Control": "no-cache",
        "Accept-Encoding": "gzip, deflate",
        "X-Timestamp": getFormattedTime(),
        "X-TUID": tuid ?? generateTuid(),
        "Key-Version": "1.0",
        "Token": "test-token"
    };

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: headers,
            body: JSON.stringify(body)
        });

        return {
            status: response.status,
            success: response.ok
        };
    } catch (error) {
        console.error('API 요청 실패:', error);
        return {
            status: 0,
            success: false
        };
    }
}

export async function sendIgnitionOn(
    baseUrl: string,
    payload: IgnitionPayload,
    tuid: string
): Promise<{ status: number; success: boolean }> {
    return sendRequest(baseUrl, "/api/ignition/on", payload, tuid);
}

export async function sendIgnitionOff(
    baseUrl: string,
    payload: IgnitionPayload,
    tuid: string
): Promise<{ status: number; success: boolean }> {
    return sendRequest(baseUrl, "/api/ignition/off", payload, tuid);
}

export async function sendCycleLog(
    baseUrl: string,
    payload: CycleLogPayload,
    tuid: string
): Promise<{ status: number; success: boolean }> {
    return sendRequest(baseUrl, "/api/cycle-log", payload, tuid);
}
