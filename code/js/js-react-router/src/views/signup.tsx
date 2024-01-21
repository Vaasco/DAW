import React, {useState, useEffect, useContext} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";
import {Navigate} from "react-router-dom";
import {fontStyle} from "../utils/styles";
import {context} from "../utils/AuthContainer";
import {CreateButton, CreateStringInput} from "../utils/models";
import {errorHandler} from "../utils/errorHandler";
export function SignUp() {
    const [inputs, setInputs] = useState({username: "", password: ""});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState("");
    const [signupSuccess, setSignupSuccess] = useState(false);
    const signupContext = useContext(context)


    const handleChange = (e) => {
        e.preventDefault();
        const {name, value} = e.target;
        setInputs((prevInputs) => ({...prevInputs, [name]: value}));
    };

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
                    errorHandler(body.error,() => {setSubmitting(false)})
                }

                if (rsp.ok) {
                    setSignupSuccess(true);
                    setSubmitting(false);
                    signupContext.username = inputs.username
                    signupContext.id = body.properties.userId
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
                        <CreateStringInput
                            type={"text"}
                            label={"username"}
                            display={"Username"}
                            id={"username"}
                            name={"username"}
                            value={inputs.username}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <CreateStringInput
                            type={"password"}
                            label={"password"}
                            display={"Password"}
                            id={"password"}
                            name={"password"}
                            value={inputs.password}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <CreateButton onClick={()=>{}} label={"Sign Up"} type={"submit"} testId={'teste'} />
                    </div>
                </fieldset>
            </form>
        </div>
    );
}
