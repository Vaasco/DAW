import React, {useState} from "react";
import {Link, Navigate} from "react-router-dom";
import Cookies from 'js-cookie';
import {Navbar} from "../utils/navBar";

export function Sign() {
    const [inputs, setInputs] = useState({username: '', password: ''});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');
    const [redirectToHome, setRedirectToHome] = useState(false);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setInputs((prevInputs) => ({...prevInputs, [name]: value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            setSubmitting(true);

            const requestBody = JSON.stringify({
                username: inputs.username,
                password: inputs.password,
            });

            const response = await fetch('http://localhost:8081/api/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: requestBody,
            });

            if (response.ok) {
                const data = await response.json();
                Cookies.set('authToken', data.properties.token)
                setRedirectToHome(true)
                console.log('Authentication successful:', data);

            } else {
                console.error('Authentication failed:', response.statusText);
                setError('Authentication failed. Please check your credentials.');
            }
        } catch (error) {
            console.error('Error during authentication:', error.message);
            setError('Error during authentication. Please try again.');
        } finally {
            setSubmitting(false);
        }
    };

    if (redirectToHome) return <Navigate to="/"/>;

    return (
        <div>
            <Navbar />
            <Link to="/">Home</Link>
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
            {error && <p style={{color: 'red'}}>{error}</p>}
        </div>
    );
}