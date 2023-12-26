import React, {useContext, useEffect, useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";
import {Navigate} from "react-router-dom";
import {fontStyle} from "../utils/styles";
import {context} from "../utils/AuthContainer";
import {CreateButton, CreateRadioInput} from "../utils/models";

export function CreateLobby() {
    const lobbyContext = useContext(context)
    const [rules, setRules] = useState('Pro');
    const [variant, setVariant] = useState('Freestyle');
    const [boardSize, setBoardSize] = useState(15);
    const [submitting, setSubmitting] = useState(false)
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

        setSubmitting(true)

        if (body.properties) {
            setSubmitting(false)
            setGameId(body.properties.id)
        }
    }

    /*const handleCancel = async () => {
        const requestBody = {
            lobbyId:
        };

        const rsp = await useFetch("/games/cancel","POST",requestBody)
        const body = await rsp.json()

        if (!rsp.ok) {
            setError(body.properties)
        }

        if (body.properties) {
            setSubmitting(false)
        }
    }*/

    useEffect(() => {
        const period = 2000;
        if (submitting) {
            const tid = setInterval(async () => {
                const username = lobbyContext.username
                const rsp3 = await useFetch(`games/user/${username}`)
                const body3 = await rsp3.json()
                if (body3.properties) {
                    setSubmitting(false)
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
            {!submitting && (
                <div>
                    Choose the rules:
                    <div>
                        <CreateRadioInput name={"Pro"} checked={rules === 'Pro'} onChange={handleRulesChange}/>
                        <CreateRadioInput name={"Long Pro"} checked={rules === 'Long Pro'}
                                          onChange={handleRulesChange}/>
                    </div>
                    <br/>
                    <div>
                        Choose the variant:
                        <div>
                            <CreateRadioInput
                                name={"Freestyle"}
                                checked={variant === 'Freestyle'}
                                onChange={handleVariantChange}
                            />
                            <CreateRadioInput
                                name={"Swap after 1st move"}
                                checked={variant === "Swap after 1st move"}
                                onChange={handleVariantChange}
                            />
                        </div>
                        <br/>
                        Choose the board size:
                        <div>
                            <CreateRadioInput name={"15"} checked={boardSize === 15} onChange={handleBoardSizeChange}/>
                            <CreateRadioInput name={"19"} checked={boardSize === 19} onChange={handleBoardSizeChange}/>
                        </div>
                    </div>
                    <br/>
                    <CreateButton onClick={handleSubmit} label={"Search for game"}/>
                </div>
            )}
        </div>
    );
}

