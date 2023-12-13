import {useFetch} from "./useFetch";
import React, {createContext, useEffect} from "react";

type contextValues = {
    username: string,
    id: number
}

const context = createContext<contextValues>({username: '', id: 0});

function AuthContainer({children}: {children: React.ReactNode}) {
    const [username, setUsername] = React.useState('')
    const [userId, setId] = React.useState(0)
    const fetchUsername = async () => {
        const rsp = await useFetch('cookies', 'GET');
        const body = await rsp.json();

        if (body.size > 0) {
            const usernameCookie = body.find(cookie => cookie.name === 'username').value;
            const idCookie = body.find(cookie => cookie.name === 'id').value;
            setUsername(usernameCookie);
            setId(idCookie);
        }
    };


    useEffect(() => {
        fetchUsername()
    }, []);

    return (
        <context.Provider value={{username: username, id: userId}}>
            {children}
        </context.Provider>
    )

}

export {context, AuthContainer}