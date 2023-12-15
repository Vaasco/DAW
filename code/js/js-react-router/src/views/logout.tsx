import React, {useContext, useEffect, useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navigate} from "react-router-dom";
import {context} from "../utils/AuthContainer";

export function Logout() {
    const logoutContext = useContext(context)
    const [body, setBody] = useState(null)

    const fetch = async () => {
        const fetch = await useFetch("users/logout","POST")
        const response = await fetch.json()
        setBody(response)
        logoutContext.username = ""
        logoutContext.id = 0
    }

    useEffect(() => {
        fetch()
    }, []);

    return (
        <div>
            {body ? (
                <div>
                    <h1><Navigate to="/" replace={true}/></h1>
                </div>
            ) :
                <div>
                    <h1>Logging out...</h1>
                </div>
            }
        </div>
    );
}
