import React, {useEffect, useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navigate} from "react-router-dom";

export function Logout() {
    const [body, setBody] = useState(null)

    useEffect(() => {
        const fetch = async () => {
            const fetch = await useFetch("/users/logout","POST",{})
            const response = await fetch.json()
            setBody(response)
        }
        fetch()
    }, []);

    return (
        <div>
            {body && (
                <div>
                    <h1><Navigate to="/" replace={true}/></h1>
                </div>
            )}
        </div>
    );
}
