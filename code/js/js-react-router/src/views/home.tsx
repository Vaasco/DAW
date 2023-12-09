import React from 'react'
import { Link } from "react-router-dom";
import {Navbar} from "../utils/navBar";
import{fontStyle} from "../utils/styles";

export function Home() {
    return (
        <div style={fontStyle}>
            <Navbar />
            <h1>Home</h1>
            <div>
                <Link to="/login">Login</Link>
                <br/>
                <Link to="/sign">Sign Up</Link>
                <br/>
                <Link to="/lobby">Create Lobby</Link>
                <br/>
                <Link to="/games">Get Game By Id</Link>
            </div>
        </div>
    );
}

