import {useLoaderData} from "react-router-dom";
import React from "react";

type Author = {
    name: string,
    number: string
}


async function authorsLoader(): Promise<Author[]> {
    const res = await fetch("http://localhost:8081/api/authors");
    const data = await res.json();
    return data.properties
}

function Authors() {
    const authors = useLoaderData() as Author[];
    return (
        <div>
            <h1>Authors</h1>
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

export {authorsLoader, Authors};