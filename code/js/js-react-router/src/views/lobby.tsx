import React, {useEffect, useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";
import {Navigate} from "react-router-dom";

export function CreateLobby() {
    const [rules, setRules] = useState('Pro');
    const [variant, setVariant] = useState('Freestyle');
    const [boardSize, setBoardSize] = useState(15);
    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState('')
    //const [pageDesign, setPageDesign] = useState<PageDesign>(PageDesign.Default);

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
        setSubmitting(true)
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
            window.location.href = `/games/${body.properties.id}`
        }
    }

    useEffect(() => {
        const period = 2000;
        if (submitting) {
            const tid = setInterval(async () => {
                const username = document.cookie.replace(/(?:(?:^|.*;\s*)username\s*\=\s*([^;]*).*$)|^.*$/, "$1");
                const rsp3 = await useFetch(`games/user/${username}`)
                const body3 = await rsp3.json()
                if (body3.properties) {
                    setSubmitting(false)
                    window.location.href = `games/${body3.properties.id}`;
                }
            }, period);
            return () => clearInterval(tid);
        }
    }, [submitting]);


    return (
        <div>
            <Navbar/>
            {submitting && (
                <div>
                    <h1>Submitting...</h1>
                </div>
            )}
            {error && (
                <div>
                    <h1> Error ${error} </h1>
                </div>
            )}
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
            </div>
        </div>
    );
}

