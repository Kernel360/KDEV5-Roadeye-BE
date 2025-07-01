import type { CycleLogPayload, IgnitionOffPayload, IgnitionOnPayload, IgnitionPayload } from '~/types/vehicle';
import { getFormattedTime } from '~/utils/vehicleUtils';

type SuccessResponse<D = unknown> = {
    success: true;
    data?: D;
}
type ErrorResponse<D = unknown> = {
    success: false;
    data?: D;
    error?: string;
}

type Response<D = unknown> = SuccessResponse<D> | ErrorResponse<D>;

async function sendRequest(
    method: string,
    baseUrl: string,
    endpoint: string,
    body: IgnitionPayload | CycleLogPayload,
    tuid: string | null
): Promise<Response> {
    const url = `${baseUrl}${endpoint}`;

    const headers = {
        "Content-Type": "application/json; charset=utf-8",
        "Accept": "application/json; charset=utf-8",
        "Cache-Control": "no-cache",
        "Accept-Encoding": "gzip, deflate",
        "X-Timestamp": getFormattedTime(),
        ...(tuid && { "X-TUID": tuid })
    };

    try {
        const response = await fetch(url, {
            method: method,
            headers: headers,
            body: JSON.stringify(body)
        });
        const data = await response.json() as { rstCd: string, rstMsg: string };

        if (response.ok && data.rstCd === "000") {
            return {
                success: true,
                data: data
            } as SuccessResponse;
        }
        else {
            return {
                success: false,
                error: data.rstMsg,
                data: data
            } as ErrorResponse;
        }
    } catch (error) {
        console.error('API 요청 실패:', error);
        return {
            success: false,
            error: error instanceof Error ? error.message : 'Unknown error',
            data: undefined
        } as ErrorResponse;
    }
}

export async function sendIgnitionOn(
    baseUrl: string,
    payload: IgnitionOnPayload,
    tuid: string
): Promise<Response> {
    return sendRequest("POST", baseUrl, "/api/ignition/on", payload, tuid);
}

export async function sendIgnitionOff(
    baseUrl: string,
    payload: IgnitionOffPayload,
    tuid: string
): Promise<Response> {
    return sendRequest("POST", baseUrl, "/api/ignition/off", payload, tuid);
}

export async function sendCycleLog(
    baseUrl: string,
    payload: CycleLogPayload,
    tuid: string
): Promise<Response> {
    return sendRequest("POST", baseUrl, "/api/cycle-log", payload, tuid);
}
