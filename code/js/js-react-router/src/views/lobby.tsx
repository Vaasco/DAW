import React, {useEffect, useState} from "react";
import {Navbar} from "../utils/navBar";
import {useFetch} from "../utils/useFetch";

enum PageDesign {
    Default,
    Waiting,
    Playing
}

export function CreateLobby() {
    const [rules, setRules] = useState('Pro');
    const [variant, setVariant] = useState('Freestyle');
    const [boardSize, setBoardSize] = useState(15);
    const [submitting, setSubmitting] = useState(false)
    const [response, setResponse] = useState(null)
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

    /*const handleSubmit = async () => {
        try {
            const requestBody = {
                rules,
                variant,
                boardSize,
            };

            const response = useFetch("games", "POST", requestBody)

            if (response == null) {
                setPageDesign(PageDesign.Waiting)

            } else {
                setPageDesign(PageDesign.Playing)
            }

        } catch (error) {
            console.error('Error creating lobby:', error.message);
            setError('Error creating lobby. Please try again.');
        }
    };*/

    function handleSubmit() {
        setSubmitting(true)
    }

    function Create() {
        const requestBody = {
            rules,
            variant,
            boardSize,
        };
        const fetch = useFetch("games", "POST", requestBody)
        const rsp = fetch.response

        useEffect(() => {
            const period = 2000;
            if (rsp && rsp.properties) {
                const tid = setInterval(() => {
                    const fetch2 = useFetch(`games/${rsp.properties.id}`);
                    if (fetch2.response) {
                        window.location.href = `games/${rsp.properties.id}`;
                    }
                }, period);
                return () => clearInterval(tid);
            }
        }, [rsp]); // Make sure to include all dependencies in the dependency array




        console.log(rsp)
        const error = fetch.error

        if (!rsp && !error) {
            return (
                <div>
                    <h1>Waiting for opponent</h1>
                </div>
            )
        }

        //Fazer polling
        /*const period = 2000
        //window.location.href = `/`
        useEffect(() => {
            if (rsp && rsp.properties) {
                const tid = setInterval(() => {
                    const fetch2 = useFetch(`games/${rsp.properties.id}`)
                    if (fetch2.response) {
                        window.location.href = `games/${rsp.properties.id}`
                    }
                }, period)
                return () => clearInterval(tid);
            }
        });*/

        if (error) {
            alert(error)
            window.location
        }
    }

    return (
        <div>
            <Navbar/>
            {submitting ?
                <div>
                    <Create/>
                </div>
                :
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
            }
        </div>
    );
}

