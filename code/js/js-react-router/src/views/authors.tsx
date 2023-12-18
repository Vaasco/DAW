import React, {useEffect, useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";
import {fontStyle} from "../utils/styles";

export function Authors() {
    const [authors, setAuthors] = useState(null)
    useEffect(() => {
        const fetchData = async () => {
                const rsp = await useFetch("authors");
                const body = await rsp.json();
                if (rsp.ok) {
                    setAuthors(body.properties);
                }
        };
        fetchData();
    }, []);

    return (
        <div style={fontStyle}>
            <Navbar/>
            {!authors ?
                <div>
                    <h1>Loading</h1>
                </div>
                :
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
            }
        </div>
    )
}