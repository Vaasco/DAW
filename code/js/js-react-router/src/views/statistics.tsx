import React, {useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";
import {useLoaderData} from "react-router-dom";
import {fontStyle, inputStyle, buttonStyle} from "../utils/styles";

async function statsLoader(): Promise<Stat[]>{
    const fetch = await useFetch('/stats')
    const body = await fetch.json()
    return body.properties
}

type Stat = {
    username: string,
    rank: number,
    playedGames: number,
    wonGames: number,
    lostGames: number
}

function GetStats() {
    const [username, setUsername] = useState('');
    const allStats = useLoaderData() as Stat[];
    const [stats, setStats] = useState(allStats);

    const getUsernameStats = (e) => {
        e.preventDefault();

        if (username.trim() === '') {
            setStats(allStats);
        } else {
            const userStats = allStats.filter((stat) => stat.username.startsWith(username));
            setStats(userStats);
        }
    };

    const lineStyle = { textAlign: 'center', border: '3px solid #000', padding: '8px' } as const;
    const usernameStyle = { textAlign: 'left', border: '3px solid #000', padding: '8px' } as const;

    return (
        <div style={fontStyle}>
            <Navbar />
            <form onSubmit={getUsernameStats}>
                <label>
                    Search for username:
                    <input style={inputStyle}
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </label>
                <button style={buttonStyle} type="submit">Search</button>
            </form>
            <p/>
            <table style={{ borderCollapse: 'collapse', width: '40%', background: '#D3D3D3' }}>
                <thead>
                <tr>
                    <th style={usernameStyle}>Username</th>
                    <th style={lineStyle}>Rank</th>
                    <th style={lineStyle}>Played Games</th>
                    <th style={lineStyle}>Won Games</th>
                    <th style={lineStyle}>Lost Games</th>
                </tr>
                </thead>
                <tbody>
                {stats.map((playerStats, index) => (
                    <tr key={index}>
                        <td style={usernameStyle}>{playerStats.username}</td>
                        <td style={lineStyle}>{playerStats.rank}</td>
                        <td style={lineStyle}>{playerStats.playedGames}</td>
                        <td style={lineStyle}>{playerStats.wonGames}</td>
                        <td style={lineStyle}>{playerStats.lostGames}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export { GetStats, statsLoader }

