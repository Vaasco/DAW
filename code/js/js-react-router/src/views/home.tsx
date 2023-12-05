import React from 'react'
import { Link } from "react-router-dom";

export function Home() {
    return (
        <div>
            <h1>Home</h1>
            <div>
                <Link to="/login">login</Link>
                <br/>
                <Link to="/sign">Sign In</Link>
                <br/>
                <Link to="/play">Create Lobby</Link>
                <br/>
                <Link to="/authors">About Us</Link>
                <br/>
                <Link to="/stats">Statistics</Link>
                <br/>
                <Link to="/game">Get Game By Id</Link>
            </div>
        </div>
    );
}

