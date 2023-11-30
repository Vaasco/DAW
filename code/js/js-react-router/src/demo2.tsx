import React, {useEffect, useState} from 'react'
import {createRoot} from 'react-dom/client'
import {createBrowserRouter, Link, Navigate, RouterProvider, useLoaderData} from "react-router-dom";


type Author = {
    name: string,
    number: string
}

const router = createBrowserRouter(
    [
        {
            path: "/",
            element: < Home/>,
        },
        {
            path: "/authors",
            element: <Authors/>,
            loader: authorsLoader
        },
        {
            path: "/login",
            element: <Login/>,
            //loader: loginLoader
        },
        {
            path: "/sign",
            element: <Sign/>
        }
    ]
)

function Login(): React.ReactElement {
    const [inputs, setInputs] = useState({username: "", password: ""})
    const [submiting, setSubmiting] = useState(false)
    const [error, setError] = useState('')
    const [redirect, setRedirect] = useState(false)

    if (redirect) return <Navigate to="/" replace={true}/>
}


async function authorsLoader(): Promise<Author[]> {
    const res = await fetch("http://localhost:8081/api/authors");
    return await res.json();
}


function Authors() {
    const authors = useLoaderData() as Author[];

    return (
        <div>
            <h1>Author</h1>
            <div>
                {authors.map((author, index) => (
                    <div key={index} style={{marginBottom: '10px'}}>
                        <strong>{author.name}</strong>
                        <p>Number: {author.number}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}


function Sign() {
    const [inputs, setInputs] = useState({username: '', password: ''});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');

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

    return (
        <div>
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
                        <button type="submit">Login</button>
                    </div>
                </fieldset>
            </form>
            {error && <p style={{color: 'red'}}>{error}</p>}
        </div>
    );
}


function Home() {
    return (
        <div>
            <h1>Home</h1>
            <div>
                <Link to="/login">Login</Link>
                <br/>
                <Link to="/sign">Sign In</Link>
                <br/>
                <Link to="/authors">About Us</Link>
            </div>
        </div>
    );
}


export function demo() {
    const root = createRoot(document.getElementById("container"))
    root.render(<RouterProvider router={router}/>)
} 