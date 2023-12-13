import React, {useContext} from 'react';
import {Link}  from 'react-router-dom';
import {navbarStyle, linkStyle, buttonStyle, linkContainerStyle} from "./styles";
import {context} from "./AuthContainer";


export function Navbar() {
    const navContext = useContext(context)

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
                {navContext.username === '' ? (
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
                        <span style={linkStyle}>{navContext.username}</span>
                        <Link to="/logout" style={linkStyle}>
                            <button style={buttonStyle}>Log out</button>
                        </Link>
                    </>
                )}
            </div>

        </nav>
    );
}
