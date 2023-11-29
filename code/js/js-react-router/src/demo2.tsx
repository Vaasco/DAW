import React, {useEffect, useState} from 'react'
import {createRoot} from 'react-dom/client'
import {createBrowserRouter, Link, RouterProvider, useLoaderData} from "react-router-dom";


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
        }
    ]
)

async function authorsLoader(): Promise<Author[]> {
    const res = await fetch("http://localhost:8080/home/authors");
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


function Home() {
    return (
        <div>
            <h1>Home</h1>
            <Link to={"/authors"}>About us</Link>
        </div>
    );
}


export function demo() {
    const root = createRoot(document.getElementById("container"))
    root.render(<RouterProvider router={router}/>)
} 