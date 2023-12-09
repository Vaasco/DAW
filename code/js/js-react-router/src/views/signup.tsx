import React, {useState, useEffect} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";
import {Navigate} from "react-router-dom";
import toastr from 'toastr'
import {fontStyle} from "../utils/styles";

export function SignUp() {
    const [inputs, setInputs] = useState({username: "", password: ""});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState("");
    const [signupSuccess, setSignupSuccess] = useState(false);

    const handleChange = (e) => {
        e.preventDefault();
        const {name, value} = e.target;
        setInputs((prevInputs) => ({...prevInputs, [name]: value}));
    };

    const errorHandler = (error) => {
        toastr.options = {
            positionClass: 'toast-center',
            progressBar: true,
            closeButton: true,
            preventDuplicates: true,
            timeOut: 5000,
            extendedTimeOut: 1000,
            iconClass: 'custom-error-icon',
            onHidden: () => setSubmitting(false)
        }
        toastr.error(error)
    }

    useEffect(() => {
        const fetchData = async () => {
            if (submitting) {
                const rsp = await useFetch("users", "POST", {
                    username: inputs.username,
                    password: inputs.password,
                });
                const body = await rsp.json();

                if (!rsp.ok) {
                    setError(body.properties);
                    errorHandler(body.error)
                }

                if (rsp.ok) {
                    // Set signup success flag
                    setSignupSuccess(true);
                    setSubmitting(false);
                }
            }
        };

        fetchData();
    }, [submitting, inputs]);

    const handleSubmit = (e) => {
        e.preventDefault();
        setSubmitting(true);
    };

    return (
        <div style={fontStyle}>
            <Navbar/>
            {error && (
                <div>
                    <h1>Error: ${error}</h1>
                </div>
            )}
            {signupSuccess && <Navigate to="/" replace={true}/>}
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
                        <button type="submit">Sign Up</button>
                    </div>
                </fieldset>
            </form>
        </div>
    );
}
