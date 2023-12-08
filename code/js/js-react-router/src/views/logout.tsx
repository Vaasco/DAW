import {useFetch} from "../utils/useFetch";

export function logout() {
    useFetch("logout", "POST")
}