import type { CycleLogPayload, IgnitionOffPayload, IgnitionOnPayload } from '~/types/vehicle';
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

function sendRequest(
    method: string,
    baseUrl: string,
    endpoint: string,
    body: IgnitionOnPayload | IgnitionOffPayload | CycleLogPayload,
    tuid: string | null
) {
    const url = `${baseUrl}${endpoint}`;

    const headers = {
        "Content-Type": "application/json;",
        "X-Timestamp": getFormattedTime(),
        ...(tuid && { "X-TUID": tuid })
    };

    return new Promise<SuccessResponse | ErrorResponse>((resolve, reject) => {
        fetch(url, {
            method: method,
            headers: headers,
            body: JSON.stringify(body),
            credentials: 'include'
        }).then(response => response.json() as Promise<{ rstCd: string, rstMsg: string }>)
            .then(data => {
                if (data["rstCd"] === "000") {
                    resolve({
                        success: true,
                        data: data
                    } as SuccessResponse);
                }
                else {
                    reject({
                        success: false,
                        error: data.rstMsg,
                        data: data
                    } as ErrorResponse);
                }
            })
            .catch(error => {
                console.error('API 요청 실패:', error);
                reject({
                    success: false,
                    error: error instanceof Error ? error.message : 'Unknown error',
                    data: undefined
                } as ErrorResponse);
            });
    })
}

export function sendIgnitionOn(
    baseUrl: string,
    payload: IgnitionOnPayload,
    tuid: string
) {
    return sendRequest("POST", baseUrl, "/api/ignition/on", payload, tuid);
}

export function sendIgnitionOff(
    baseUrl: string,
    payload: IgnitionOffPayload,
    tuid: string
) {
    return sendRequest("POST", baseUrl, "/api/ignition/off", payload, tuid);
}

export function sendCycleLog(
    baseUrl: string,
    payload: CycleLogPayload,
    tuid: string
) {
    return sendRequest("POST", baseUrl, "/api/cycle-log", payload, tuid);
}
