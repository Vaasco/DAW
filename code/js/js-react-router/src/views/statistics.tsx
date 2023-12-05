import React, {useState} from "react";
import {useLoaderData} from "react-router-dom";

type Stat = {
    username: string,
    rank: number,
    playedGames: number,
    wonGames: number,
    lostGames: number
}

enum StatType {
    Individual,
    All
}

async function statsLoader() {
    const res = await fetch("http://localhost:8081/api/stats");
    const data = await res.json();
    return data.properties
}

function GetStats() {
    const [username, setUsername] = useState('');
    const [individualStats, setIndividualStats] = useState(null);
    const [error, setError] = useState(null);
    const [statType, setStatType] = useState(StatType.All)
    const auth = useLoaderData() as Stat[]
    const handleIndividualStats = async (e) => {
        e.preventDefault();

        try {
            if (!username) setStatType(StatType.All)
            else {
                setStatType(StatType.Individual)
                const response = await fetch(`http://localhost:8081/api/stats/${username}`);

                if (response.ok) {
                    const data = await response.json();
                    const userStats = data.properties;
                    console.log(userStats)

                    if (userStats) {
                        setIndividualStats(userStats);
                        setError(null);
                    } else {
                        setError(`User '${username}' not found in the response.`);
                        setIndividualStats(null);
                    }
                } else {
                    const errorData = await response.json();
                    setError(`Error: ${errorData.message}`);
                    setIndividualStats(null);
                }
            }

        } catch (error) {
            console.error('Error fetching data:', error);
            setError('Error fetching data. Please try again.');
            setIndividualStats(null);
        }
    };

    return (
        <div>
            <form onSubmit={handleIndividualStats}>
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

            {statType === StatType.Individual && individualStats && (
                <div>
                    {individualStats.map((userStats, index) => (
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

            {statType === StatType.All && auth && (
                <div>
                    <h2>All Player Stats:</h2>
                    {auth.map((playerStats, index) => (
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