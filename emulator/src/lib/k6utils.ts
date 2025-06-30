import http from 'k6/http';

export async function k6Fetch(url: string, init: {
    method: string,
    body?: string,
    headers?: {
        [key: string]: string;
    }
}) {
    const res = await http.asyncRequest(
        init.method,
        url,
        init.body,
        {
            headers: init.headers,
        }
    )
    return {
        url: res.url,
        headers: res.headers,
        status: res.status,
        statusText: res.status_text,
        ok: res.status >= 200 && res.status < 300,
        json: () => res.json(),
    } as unknown as Response
}