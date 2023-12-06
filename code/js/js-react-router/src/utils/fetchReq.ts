const BASE_URL = "http://localhost:8081/api/";

export async function fetchReq(path: string, method: string = "GET", body?: Object, token?: string) {
    const headers = { "Content-Type": "application/json" };
    if (token) headers ["Authorization"] = `Bearer ${token}`
    const options = { method, headers };

    if (body) options["body"] = JSON.stringify(body);
    const url = BASE_URL + path;
    const res = await fetch(url, options);
    const data = await res.json();
    if (!res.ok) {
        console.error(`Error fetching ${path}:`, res.statusText);
        throw res;
        //throw new Error( `Error fetching game: ${res.statusText}` )
    }

    return data.properties;
}
