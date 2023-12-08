<<<<<<< HEAD
import React from "react";
import {useFetch} from "../utils/useFetch";
import {Navigate} from "react-router-dom";

export async function Logout() {
    const fetch = await useFetch("users/logout", "POST", {})
    const body = await fetch.json()

    return (
        <div>
            {body.properties && (
                <div>
                    <h1><Navigate to="/" replace={true}/></h1>
                </div>
            )}
        </div>
    );
}
