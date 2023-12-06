import React, {createContext, useContext, useState} from "react";
import Cookies from 'js-cookie';
import {Navbar} from "../utils/navBar";
import {fetchReq} from "../utils/fetchReq";
import {Simulate} from "react-dom/test-utils";
import waiting = Simulate.waiting;


enum PageDesign {
    Default,
    Waiting,
    Playing
}

type AuthContextType = {
    token: string,
    setToken: (newToken: string) => void
}

export function CreateLobby() {
    const [rules, setRules] = useState('Pro');
    const [variant, setVariant] = useState('Freestyle');
    const [boardSize, setBoardSize] = useState(15);
    const [error, setError] = useState(null);
    const [pageDesign, setPageDesign] = useState<PageDesign>(PageDesign.Default);

    //const auth = useContext(AuthContext);
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

            const response = await fetchReq("games", "POST", requestBody, authTokenCookie)

            if (response == null) setPageDesign(PageDesign.Waiting)
            else setPageDesign(PageDesign.Playing)

        } catch (error) {
            console.error('Error creating lobby:', error.message);
            setError('Error creating lobby. Please try again.');
        }
    };

    return (
        <div>
            <Navbar />
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
                                    value="Swap after 1st move"
                                    checked={variant === "Swap after 1st move"}
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

