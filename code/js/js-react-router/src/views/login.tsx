import React, {useEffect, useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";

export function Login(): React.ReactElement {
    const [inputs, setInputs] = useState({username: '', password: ''});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        e.preventDefault()
        const {name, value} = e.target;
        setInputs((prevInputs) => ({...prevInputs, [name]: value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault()
        const rsp = await useFetch("users/login", "POST", { username: inputs.username, password: inputs.password })
        const body = await rsp.json()

        if(!rsp.ok){
            setError(body.properties)
        }

        if(rsp.ok){
            window.location.href = "/"
        }
    }

    return (
        <div>
            <Navbar/>
            {error && (
                <div>
                    <h1>Error: ${error}</h1>
                </div>
            )}
                <form onSubmit={handleSubmit}>
                    <fieldset disabled={submitting}>
                        <div>
                            <label htmlFor="username">Username</label>
                            <input
                                id="username"
                                type="text"
                                name="username"
                                value={inputs.username}
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <label htmlFor="password">Password</label>
                            <input
                                id="password"
                                type="password"
                                name="password"
                                value={inputs.password}
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <button type="submit">Login</button>
                        </div>
                    </fieldset>
                </form>
        </div>
    );
}
