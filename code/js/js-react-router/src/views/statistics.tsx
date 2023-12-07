import React, {useState} from "react";
import {useLoaderData} from "react-router-dom";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";

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
    const [nameStats, setNameStats] = useState(null);
    const [everyStats, setEveryStats] = useState(null);
    const [error, setError] = useState(null);
    const [statType, setStatType] = useState(StatType.All)
    //const auth = useLoaderData() as Stat[]
    const handleIndividualStats = (e) => {
        e.preventDefault();

        /*try {
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
        }*/
    };

    function allStats() {
        setNameStats(null)
        const fetchAll = await useFetch('/stats')
        const body = await fetchAll.json()
        if (!fetchAll.ok) {
            setError(body.properties)
        }

        if (fetchAll.ok) {
            setEveryStats(body.properties)
        }

        if (error) {
            alert(error)
            window.location
        }
    }

    const usernameStats = () => {
        setEveryStats(null)
        const fetchAll = await useFetch(`/stats/${username}`)
        const body = await fetchAll.json()

        if (!fetchAll.ok) {
            setError(body.properties)
        }

        if (fetchAll.ok) {
            setNameStats(body.properties)
        }
    }

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