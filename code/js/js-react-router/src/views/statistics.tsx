import React, {useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";
import {useLoaderData} from "react-router-dom";

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
    const [error, setError] = useState(null);
    const allStats = useLoaderData() as Stat[]
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

    return (
        <div>
            <Navbar/>
            <form onSubmit={usernameStats}>
                <label>
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </label>
                <button type="submit">Get Stats</button>
            </form>

            {nameStats && (
                <div>
                    {nameStats.map((userStats, index) => (
                        <div key={index}>
                            <h2>Username: {userStats.username}</h2>
                            <p>Rank: {userStats.rank}</p>
                            <p>Played Games: {userStats.playedGames}</p>
                            <p>Won Games: {userStats.wonGames}</p>
                            <p>Lost Games: {userStats.lostGames}</p>
                        </div>
                    ))}
                </div>
            )}

            {everyStats && (
                <div>
                    <h2>All Player Stats:</h2>
                    {everyStats.map((playerStats, index) => (
                        <div key={index}>
                            <h3>{playerStats.username}</h3>
                            <p>Rank: {playerStats.rank}</p>
                            <p>Played Games: {playerStats.playedGames}</p>
                            <p>Won Games: {playerStats.wonGames}</p>
                            <p>Lost Games: {playerStats.lostGames}</p>
                        </div>
                    ))}
                </div>
            )}

            {error && <p style={{color: 'red'}}>{error}</p>}
        </div>
    );
}

export {GetStats, statsLoader}