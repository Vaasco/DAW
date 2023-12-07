import React, {useEffect, useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";

export function SignIn() {
    const [inputs, setInputs] = useState({username: '', password: ''});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        e.preventDefault()
        const {name, value} = e.target;
        setInputs((prevInputs) => ({...prevInputs, [name]: value}));
    };

    const handleSubmit = (e) => {
        e.preventDefault()
        setSubmitting(true)
    };

    function Create(input) {
        useEffect(() => {
            setSubmitting(false)
        }, []);
        const fetch = useFetch("users", "POST", {username: input.username, password: input.password})
        const rsp = fetch.response
        const error = fetch.error
        if (!rsp && !error) {
            return (
                <div>
                    <h1>Loading</h1>
                </div>
            )
        }

        if (rsp) {
            window.location.href = "/"
        }

        if (error) {
            alert(error)
            window.location
        }

    }

    return (
        <div>
            <Navbar/>
            {!submitting?
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
                            <button type="submit">Sign In</button>
                        </div>
                    </fieldset>
                </form>
        </div>
    );
}