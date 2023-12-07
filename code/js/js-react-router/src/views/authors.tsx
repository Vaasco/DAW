import {useLoaderData} from "react-router-dom";
import React, {useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";

export function Authors() {
    const fetch = useFetch("authors").response

    return (
        <div>
            <Navbar/>
            {!fetch ?
                <div>
                    <h1>Loading</h1>
                </div>
                :
                <div>
                    <h1>Authors</h1>
                    <div>
                        {fetch.properties.map((author, index) => (
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