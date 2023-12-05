const BASE_URL = "http://localhost:8081/api/";

export async function fetchReq(path: string, method: string = "GET", body?: Object) {
    const headers = { "Content-Type": "application/json" };
    const options = { method, headers};

    if (body) options["body"] = JSON.stringify(body);
    const url = BASE_URL + path;
    const res = await fetch(url, options);
    const data = await res.json();
    if (!res.ok) {
        console.error('Error fetching game:', res.statusText);
        throw res;
    }

    return data.properties;
}
