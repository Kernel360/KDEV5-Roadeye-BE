
////

export async function login(
    baseUrl: string,
    companyId: string,
    username: string,
    password: string
): Promise<Response> {
    const headers = {
        'Content-Type': 'application/json',
        "X-Company-Id": companyId
    };

    return await fetch(`${baseUrl}/api/auth/sign-in`, {
        method: "POST",
        headers,
        body: JSON.stringify({ username, password }),
        credentials: "include"
    });
}

export async function getMyInfo(
    baseUrl: string,
) {
    return await fetch(`${baseUrl}/api/employees/my`, {
        method: "GET",
        credentials: "include"
    });
}

export async function getAllCarList(
    baseUrl: string,
) {
    return await fetch(`${baseUrl}/api/cars/all`, {
        method: "GET",
        credentials: "include"
    });
}
