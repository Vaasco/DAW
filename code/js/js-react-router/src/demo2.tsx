import React, {Context, createContext, useContext, useEffect, useState} from 'react'
import {createRoot} from 'react-dom/client'
import {createBrowserRouter, Link, Navigate, RouterProvider, useLoaderData} from "react-router-dom";
import Cookies from 'js-cookie';


type Author = {
    name: string,
    number: string
}

type AuthContextType = {
    token: string,
    setToken: (newToken: string) => void
}

const AuthContext = createContext<AuthContextType>({
    token: '', setToken: () => {
    }
})

const router = createBrowserRouter(
    [
        {
            path: "/",
            element: < Home/>,
        },
        {
            path: "/authors",
            element: <Authors/>,
            loader: authorsLoader
        },
        {
            path: "/login",
            element: <Login/>,
        },
        {
            path: "/sign",
            element: <Sign/>
        },
        {
            path: "/play",
            element: <CreateLobby/>
        },
        {
            path: "/stats",
            element: <GetStats/>
        },
        {
            path: "/search",
            element: <SearchUser/>
        }
    ]
)

function SearchUser() {


    return (
        <div>
            Ainda não sei se devo utilizar isto pq acho que nao podemos mostrar id!
        </div>
    )
}

function GetStats() {
    const [username, setUsername] = useState('');
    const [statsType, setStatsType] = useState('individual'); // New state for stats type
    const [individualStats, setIndividualStats] = useState(null);
    const [allStats, setAllStats] = useState(null);
    const [error, setError] = useState(null);

    const handleIndividualStats = async (e) => {
        e.preventDefault();
        setStatsType('individual'); // Set stats type to individual

        try {
            const response = await fetch(`http://localhost:8081/api/stats/${username}`);

            if (response.ok) {
                const data = await response.json();
                const userStats = data.properties;

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
        } catch (error) {
            console.error('Error fetching data:', error);
            setError('Error fetching data. Please try again.');
            setIndividualStats(null);
        }
    };

    const handleLoadAllStats = async () => {
        setStatsType('all'); // Set stats type to all

        try {
            const response = await fetch('http://localhost:8081/api/stats');

            if (response.ok) {
                const data = await response.json();
                setAllStats(data.properties);
                setError(null);
            } else {
                const errorData = await response.json();
                setError(`Error: ${errorData.message}`);
                setAllStats(null);
            }
        } catch (error) {
            console.error('Error fetching data:', error);
            setError('Error fetching data. Please try again.');
            setAllStats(null);
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

            {statsType === 'individual' && individualStats && (
                <div>
                    <h2>Username: {individualStats.username}</h2>
                    <p>Rank: {individualStats.rank}</p>
                    <p>Played Games: {individualStats.playedGames}</p>
                    <p>Won Games: {individualStats.wonGames}</p>
                    <p>Lost Games: {individualStats.lostGames}</p>
                </div>
            )}

            {statsType === 'all' && allStats && (
                <div>
                    <h2>All Player Stats:</h2>
                    {allStats.map((playerStats, index) => (
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

            <button onClick={handleLoadAllStats}>Load All Stats</button>
        </div>
    );
}


export default GetStats;


function CreateLobby() {
    const [playerId, setPlayerId] = useState(1);
    const [rules, setRules] = useState('Pro');
    const [variant, setVariant] = useState('Freestyle');
    const [boardSize, setBoardSize] = useState(15);
    const auth = useContext(AuthContext)

    const authTokenCookie = Cookies.get('authToken')
    const handleRulesChange = (e) => {
        setRules(e.target.value);
    };

    const handleVariantChange = (e) => {
        setVariant(e.target.value);
    };

    const handleBoardSizeChange = (e) => {
        setBoardSize(parseInt(e.target.value, 10));
    };

    const handleSubmit = async () => {
        try {
            const requestBody = {
                playerId,
                rules,
                variant,
                boardSize,
            };

            const response = await fetch('http://localhost:8081/api/games', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authTokenCookie}`,
                },
                body: JSON.stringify(requestBody),
            });

            if (response.ok) {
                const data = await response.json();
                console.log('Create Lobby successful:', data);
            } else {
                console.error('Create Lobby failed:', response.statusText);
                // Handle the error as needed
            }
        } catch (error) {
            console.error('Error creating lobby:', error.message);
            // Handle the error as needed
        }
    };


    return (
        <div>
            <label>
                Rules:
                <div>
                    <input
                        type="radio"
                        value="Pro"
                        checked={rules === 'Pro'}
                        onChange={handleRulesChange}
                    />
                    Pro
                    <input
                        type="radio"
                        value="Long Pro"
                        checked={rules === 'Long Pro'}
                        onChange={handleRulesChange}
                    />
                    Long Pro
                </div>
            </label>
            <br/>

            <label>
                Variant:
                <div>
                    <input
                        type="radio"
                        value="Freestyle"
                        checked={variant === 'Freestyle'}
                        onChange={handleVariantChange}
                    />
                    Freestyle
                    <input
                        type="radio"
                        value="Swap"
                        checked={variant === 'Swap'}
                        onChange={handleVariantChange}
                    />
                    Swap
                </div>
            </label>
            <br/>

            <label>
                Board Size:
                <div>
                    <input
                        type="radio"
                        value={15}
                        checked={boardSize === 15}
                        onChange={handleBoardSizeChange}
                    />
                    15
                    <input
                        type="radio"
                        value={19}
                        checked={boardSize === 19}
                        onChange={handleBoardSizeChange}
                    />
                    19
                </div>
            </label>
            <br/>

            <button onClick={handleSubmit}>Submit</button>
        </div>
    );
}

function Login(): React.ReactElement {
    const [inputs, setInputs] = useState({username: '', password: ''});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');
    const [token, setToken] = useState('');
    //const auth = useContext(AuthContext);


    const handleChange = (e) => {
        const {name, value} = e.target;
        setInputs((prevInputs) => ({...prevInputs, [name]: value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();


        try {
            setSubmitting(true);

            const requestBody = JSON.stringify({
                username: inputs.username,
                password: inputs.password,
            });

            const response = await fetch('http://localhost:8081/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: requestBody,
            });
            if (response.ok) {
                const data = await response.json();
                const newToken = data.properties.token;
                Cookies.set('authToken', newToken)
                //auth.setToken(newToken)
                console.log("ESTE É O TOKEN NOVO!!!", newToken);
            } else {
                console.error('Authentication failed:', response.statusText);
                setError('Authentication failed. Please check your credentials.');
            }
        } catch (error) {
            console.error('Error during authentication:', error.message);
            setError('Error during authentication. Please try again.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div>
            <Link to="/">Home</Link>
            <form onSubmit={handleSubmit}>
                <fieldset disabled={submitting}>
                    <div>
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            type="text"
                            name="username"
                            value={inputs.username}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            name="password"
                            value={inputs.password}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <button type="submit">Login</button>
                    </div>
                </fieldset>
            </form>
            {error && <p style={{color: 'red'}}>{error}</p>}
        </div>
    );
}


async function authorsLoader(): Promise<Author[]> {
    const res = await fetch("http://localhost:8081/api/authors");
    const data = await res.json();
    return data.properties
}


function Authors() {
    const authors = useLoaderData() as Author[];
    return (
        <div>
            <h1>Authors</h1>
            <div>
                {authors.map((author, index) => (
                    <div key={index} style={{marginBottom: '10px'}}>
                        <strong>{author.name}</strong>
                        <p>Number: {author.number}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}


function Sign() {
    const [inputs, setInputs] = useState({username: '', password: ''});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');


    const handleChange = (e) => {
        const {name, value} = e.target;
        setInputs((prevInputs) => ({...prevInputs, [name]: value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            setSubmitting(true);

            const requestBody = JSON.stringify({
                username: inputs.username,
                password: inputs.password,
            });

            const response = await fetch('http://localhost:8081/api/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: requestBody,
            });

            if (response.ok) {
                const data = await response.json();
                Cookies
                console.log('Authentication successful:', data);

            } else {
                console.error('Authentication failed:', response.statusText);
                setError('Authentication failed. Please check your credentials.');
            }
        } catch (error) {
            console.error('Error during authentication:', error.message);
            setError('Error during authentication. Please try again.');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div>
            <Link to="/">Home</Link>
            <form onSubmit={handleSubmit}>
                <fieldset disabled={submitting}>
                    <div>
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            type="text"
                            name="username"
                            value={inputs.username}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            name="password"
                            value={inputs.password}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <button type="submit">Sign In</button>
                    </div>
                </fieldset>
            </form>
            {error && <p style={{color: 'red'}}>{error}</p>}
        </div>
    );
}


function Home() {
    const [token, setToken] = useState('')
    return (
        <AuthContext.Provider value={{token, setToken}}>
            <div>
                <h1>Home</h1>
                <div>
                    <Link to="/login">Login</Link>
                    <br/>
                    <Link to="/sign">Sign In</Link>
                    <br/>
                    <Link to="/play">Create Lobby</Link>
                    <br/>
                    <Link to="/authors">About Us</Link>
                    <br/>
                    <Link to="/stats">Statistics</Link>
                    <br/>
                    <Link to="/search">Search User</Link>
                    <br/>
                    -------------//-------------
                    <br/>

                </div>
            </div>
        </AuthContext.Provider>

    );
}


export function demo() {
    const root = createRoot(document.getElementById("container"))

    root.render(
        <RouterProvider router={router}/>
    );

}