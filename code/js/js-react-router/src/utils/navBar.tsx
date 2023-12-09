// Navbar.jsx
import React from 'react';
import {Link}  from 'react-router-dom';
import {navbarStyle, linkStyle, buttonStyle, linkContainerStyle} from "./styles";

export function Navbar() {
    const username = document.cookie.replace(/(?:(?:^|.*;\s*)username\s*\=\s*([^;]*).*$)|^.*$/, "$1");

    return (
        <nav style={navbarStyle}>
            <div>
                <Link to="/" style={linkStyle}>
                    <button style={buttonStyle}>Home</button>
                </Link>
                <Link to="/stats" style={linkStyle}>
                    <button style={buttonStyle}>Statistics</button>
                </Link>
            </div>
            <div style={linkContainerStyle}>
                <Link to="/authors" style={linkStyle}>
                    <button style={buttonStyle}>About Us</button>
                </Link>
                {!username ? (
                    <>
                        <Link to="/login" style={linkStyle}>
                            <button style={buttonStyle}>Login</button>
                        </Link>
                        <Link to="/sign" style={linkStyle}>
                            <button style={buttonStyle}>Sign Up</button>
                        </Link>
                    </>
                ) : (
                    <>
                        <span style={linkStyle}>{username}</span>
                        <Link to="/logout" style={linkStyle}>
                            <button style={buttonStyle}>Log out</button>
                        </Link>
                    </>
                )}
            </div>

        </nav>
    );
}
