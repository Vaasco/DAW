import React, {Context, createContext, useContext, useEffect, useState} from 'react'
import {createRoot} from 'react-dom/client'
import {createBrowserRouter, Link, Navigate, RouterProvider, useLoaderData} from "react-router-dom";
import Cookies from 'js-cookie';


type Author = {
    name: string,
    number: string
}

type Stat = {
    username: string,
    rank: number,
    playedGames: number,
    wonGames: number,
    lostGames: number
}

type AuthContextType = {
    token: string,
    setToken: (newToken: string) => void
}

const AuthContext = createContext<AuthContextType>({
    token: '', setToken: () => {
    }
})

enum StatType {
    Individual,
    All
}

enum PageDesign {
    Default,
    Waiting,
    Playing

}

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
            element: <GetStats/>,
            loader: statsLoader
        }
    ]
)

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
            else{
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

    const handleLoadAllStats = async () => {
        //setStatsType('all'); // Set stats type to all

        try {
            const response = await fetch('http://localhost:8081/api/stats');

            if (response.ok) {
                const data = await response.json();
                setError(null);
            } else {
                const errorData = await response.json();
                setError(`Error: ${errorData.message}`);
                //setAllStats(null);
            }
        } catch (error) {
            console.error('Error fetching data:', error);
            setError('Error fetching data. Please try again.');
            //setAllStats(null);
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


function CreateLobby() {
    const [playerId, setPlayerId] = useState(1);
    const [rules, setRules] = useState('Pro');
    const [variant, setVariant] = useState('Freestyle');
    const [boardSize, setBoardSize] = useState(15);
    const [error, setError] = useState(null); // New state for error
    const [pageDesign, setPageDesign] = useState<PageDesign>(PageDesign.Default);

    const auth = useContext(AuthContext);
    const authTokenCookie = Cookies.get('authToken');

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
                rules,
                variant,
                boardSize,
            };

            const response = await fetch('http://localhost:8081/api/games', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authTokenCookie}`
                },
                body: JSON.stringify(requestBody),
            });

            if (response.ok) {
                const data = await response.json();
                console.log('Create Lobby successful:', data);
            } else {
                const errorData = await response.json();
                setError(`Error: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error creating lobby:', error.message);
            setError('Error creating lobby. Please try again.');
        }
    };

    return (
        <div>
            {pageDesign === PageDesign.Default && (
                <div>
                    Rules:
                    <div>
                        <label>
                            <input
                                type="radio"
                                value="Pro"
                                checked={rules === 'Pro'}
                                onChange={handleRulesChange}
                            />
                            Pro
                        </label>
                        <label>
                            <input
                                type="radio"
                                value="Long Pro"
                                checked={rules === 'Long Pro'}
                                onChange={handleRulesChange}
                            />
                            Long Pro
                        </label>
                    </div>
                    <br/>
                    <div>
                        Variant:
                        <div>
                            <label>
                                <input
                                    type="radio"
                                    value="Freestyle"
                                    checked={variant === 'Freestyle'}
                                    onChange={handleVariantChange}
                                />
                                Freestyle
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    value="Swap"
                                    checked={variant === 'Swap'}
                                    onChange={handleVariantChange}
                                />
                                Swap
                            </label>
                        </div>
                        <br/>
                        Board Size:
                        <div>
                            <label>
                                <input
                                    type="radio"
                                    value={15}
                                    checked={boardSize === 15}
                                    onChange={handleBoardSizeChange}
                                />
                                15
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    value={19}
                                    checked={boardSize === 19}
                                    onChange={handleBoardSizeChange}
                                />
                                19
                            </label>
                        </div>
                    </div>
                    <br/>
                    <button onClick={handleSubmit}>Submit</button>
                    {error && <p style={{color: 'red'}}>{error}</p>}
                </div>
            )}
            {pageDesign === PageDesign.Waiting && (
                <h2>Waiting for opponent...</h2>
            )}
        </div>
    );
}

function getUsername() {

}

function Login(): React.ReactElement {
    const [inputs, setInputs] = useState({username: '', password: ''});
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');
    const [loginSuccess, setLoginSuccess] = useState(false);

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
                if (Cookies.get('authToken') === newToken) {
                    setLoginSuccess(true);
                } else {
                    Cookies.set('authToken', newToken);
                    console.log("ESTE É O TOKEN NOVO!!!", newToken);
                }
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
            {loginSuccess && <p style={{color: 'green'}}>Login successful</p>}
            {!loginSuccess && (
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
            )}
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
    const [redirectToHome, setRedirectToHome] = useState(false);

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
                Cookies.set('authToken', data.properties.token)
                setRedirectToHome(true)
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

    if (redirectToHome) return <Navigate to="/"/>;

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