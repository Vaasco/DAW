const BASE_URL = "http://localhost:8081/api/"

export function useFetch(path:string, method:string = "GET", body?:object) {
    const options = {
        method: method,
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
        },
        ...(body && {body: JSON.stringify(body)}),
    };
    return fetch(BASE_URL + path, options);
}