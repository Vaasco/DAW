import React from 'react'
import { Link } from "react-router-dom";
import {Navbar} from "../utils/navBar";
import{fontStyle} from "../utils/styles";

export function Home() {
    const username = document.cookie.replace(/(?:(?:^|.*;\s*)username\s*\=\s*([^;]*).*$)|^.*$/, "$1");

    return (
        <div style={fontStyle}>
            <Navbar />
            <h1>Home</h1>
            <div>
                <Link to="/games">Get Game By Id</Link>
                <br/>
                {username && (
                    <Link to="/lobby">Create Lobby</Link>
                )}
            </div>
        </div>
    );
}

