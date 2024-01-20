import React, {useContext} from 'react'
import {Link} from "react-router-dom";
import {Navbar} from "../utils/navBar";
import {fontStyle} from "../utils/styles";
import {context} from "../utils/AuthContainer";

export function Home() {
    const homeContext = useContext(context)
    const username = homeContext.username
    return (
        <div style={fontStyle}>
            <Navbar/>
            <h1>Home</h1>
            <div>
                <Link to="/games">Get Game By Id</Link>
                <br/>
                {username && (
                    <Link to="/lobby">Start a game</Link>
                )}
            </div>
        </div>
    );
}

