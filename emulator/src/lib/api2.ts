
////

export function login(
    baseUrl: string,
    companyId: string,
    username: string,
    password: string
): Promise<Response> {
    const headers = {
        'Content-Type': 'application/json',
        "X-Company-Id": companyId
    };

    return fetch(`${baseUrl}/api/auth/sign-in`, {
        method: "POST",
        headers,
        body: JSON.stringify({ username, password }),
        credentials: "include"
    });
}

export function getMyInfo(
    baseUrl: string,
) {
    return fetch(`${baseUrl}/api/employees/my`, {
        method: "GET",
        credentials: "include"
    });
}

export function getAllCarList(
    baseUrl: string,
) {
    return fetch(`${baseUrl}/api/cars/all`, {
        method: "GET",
        credentials: "include"
    });
}
