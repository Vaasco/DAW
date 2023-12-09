// Navbar.jsx
import React from 'react';
import { Link } from 'react-router-dom';

export function Navbar() {
    const username = document.cookie.replace(/(?:(?:^|.*;\s*)username\s*\=\s*([^;]*).*$)|^.*$/, "$1");

    const navbarStyle = {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',  // Center items vertically
        padding: '10px',
        backgroundColor: 'white',
        fontFamily: 'Nova Square, sans-serif',
    };

    const linkContainerStyle = {
        display: 'flex',
        alignItems: 'center',
    };

    const linkStyle = {
        textDecoration: 'none',
        color: 'black',
        marginRight: '10px',
    };

    const buttonStyle = {
        background: 'none',
        border: 'none',
        cursor: 'pointer',
    };

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
