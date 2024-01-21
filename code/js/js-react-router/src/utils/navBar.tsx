import React, {useContext} from 'react';
import {Link} from 'react-router-dom';
import {navbarStyle, linkStyle, playButtonStyle, linkContainerStyle} from "./styles";
import {context} from "./AuthContainer";

export function Navbar() {
    const navContext = useContext(context)
    return (
        <nav style={navbarStyle}>
            <div>
                <Link to="/" style={linkStyle}>
                    <button style={playButtonStyle}>Home</button>
                </Link>
                <Link to="/stats" style={linkStyle}>
                    <button style={playButtonStyle}>Statistics</button>
                </Link>
            </div>
            <div style={linkContainerStyle}>
                <Link to="/authors" style={linkStyle}>
                    <button style={playButtonStyle}>About Us</button>
                </Link>
                {navContext.username === '' ? (
                    <>
                        <Link to="/login" style={linkStyle}>
                            <button style={playButtonStyle}>Login</button>
                        </Link>
                        <Link to="/sign" style={linkStyle}>
                            <button style={playButtonStyle}>Sign Up</button>
                        </Link>
                    </>
                ) : (
                    <>
                        <span style={linkStyle}>{navContext.username}</span>
                        <Link to="/logout" style={linkStyle}>
                            <button style={playButtonStyle}>Log out</button>
                        </Link>
                    </>
                )}
            </div>

        </nav>
    );
}
