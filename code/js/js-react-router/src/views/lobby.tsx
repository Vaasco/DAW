import React, {useContext, useEffect, useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";
import {Navigate} from "react-router-dom";
import {fontStyle} from "../utils/styles";
import {context} from "../utils/AuthContainer";

export function CreateLobby() {
    const lobbyContext = useContext(context)
    const [rules, setRules] = useState('Pro');
    const [variant, setVariant] = useState('Freestyle');
    const [boardSize, setBoardSize] = useState(15);
    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState('')
    const [gameId, setGameId] = useState(0)

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
        const requestBody = {
            rules,
            variant,
            boardSize,
        };

        const rsp = await useFetch("games", "POST", requestBody)
        const body = await rsp.json()

        if (!rsp.ok) {
            setError(body.properties)
        }

        setSubmitting(true)

        if (body.properties) {
            setSubmitting(false)
            setGameId(body.properties.id)
        }
    }

    useEffect(() => {
        const period = 2000;
        if (submitting) {
            const tid = setInterval(async () => {
                const username = lobbyContext.username
                const rsp3 = await useFetch(`games/user/${username}`)
                const body3 = await rsp3.json()
                if (body3.properties) {
                    setSubmitting(false)
                    //window.location.href = `games/${body3.properties.id}`;
                    setGameId(body3.properties.id)
                }
            }, period);
            return () => clearInterval(tid);
        }
    }, [submitting]);

    return (
        <div style={fontStyle}>
            <Navbar/>
            {submitting && (
                <div>
                    <h1>Waiting for opponent...</h1>
                </div>
            )}
            {gameId != 0 && <Navigate to={`/games/${gameId}`} replace={true}/>}
            {error && (
                <div>
                    <h1> Error ${error} </h1>
                </div>
            )}
            {!submitting && (
            <div>
                Choose the rules:
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
                    Choose the variant:
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
                    Choose the board size:
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
            </div>
            )}
        </div>
    );
}

