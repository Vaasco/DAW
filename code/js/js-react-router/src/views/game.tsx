import React, {useContext, useEffect, useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navbar} from "../utils/navBar";
import {fontStyle} from "../utils/styles";
import blackstone from "../utils/images/blackstone.png"
import whitestone from "../utils/images/whitestone.png"
import {Navigate, useParams} from "react-router-dom";
import {context} from "../utils/AuthContainer";
import {CreateButton} from "../utils/models";
import {errorHandler} from "../utils/errorHandler";

function GetGame() {
    const gameContext = useContext(context)
    const playerId = gameContext.id
    const gameId = useParams().id
    const [response, setResponse] = useState(null);
    const [played, setPlayed] = useState(false);
    const [playerB, setPlayerB] = useState('');
    const [playerBId, setPlayerBId] = useState(0);
    const [playerW, setPlayerW] = useState('');
    const [win, setWin] = useState('');
    const [swapping, setSwapping] = useState(false);
    const [forfeit, setForfeit] = useState(false);

    const fetchGame = async () => {
        if (response === null) {
            const rsp = await useFetch(`games/${gameId}`)
            const body = await rsp.json()
            const properties = body.properties
            if (rsp.ok) {
                setResponse(properties)
                setPlayerBId(properties.playerB)
            }
            if (!rsp.ok) {
                errorHandler(body.error)
            }
        }
    }

    const checkGameState = () => {
        let player = 'D'
        if (response.playerB === playerId) {
            player = 'B'
        }
        if (response.playerW === playerId) {
            player = 'W'
        }
        if (response.state) {
            if ((response.state === 'Ended B' || response.state === 'W Forfeited') && player === 'B') setWin('You Won')
            if ((response.state === 'Ended W' || response.state === 'B Forfeited') && player === 'W') setWin('You Won')
            if ((response.state === 'Ended B' || response.state === 'W Forfeited') && player === 'W') setWin('You Lost')
            if ((response.state === 'Ended W' || response.state === 'B Forfeited') && player === 'B') setWin('You Lost')
            if (response.state === 'Ended D') setWin('The game ended with a draw')
        }
    }

    const playable = response && response.state === 'Playing' &&
        ((playerId == response.playerB && response.board.turn == 'B') ||
            (playerId == response.playerW && response.board.turn == 'W'))

    const play = async (rowIndex: number, colIndex: number) => {
        const requestBody = {
            row: rowIndex,
            col: colIndex,
            swap: swapping ? 1 : null
        }

        const rsp = await useFetch(`games/${gameId}`, 'POST', requestBody)
        const body = await rsp.json()
        const properties = body.properties
        if (!rsp.ok) {
            errorHandler(body.error)
        } else {
            if (properties !== response) {
                setResponse(properties)
                if (swapping && Object.keys(properties.board.moves).length === 2) {
                    const temp = playerB
                    setPlayerB(playerW)
                    setPlayerW(temp)
                    setSwapping(false)
                    setPlayerBId(properties.playerB)
                }
            }
            checkGameState()
        }
    }

    const handleForfeit = async () => {
        const rsp = await useFetch(`games/forfeit/${gameId}`, "POST")
        const body = await rsp.json()
        if (!rsp.ok) {
            errorHandler(body.error)
        } else {
            setForfeit(true)
        }
    }

    const generateBoard = () => {
        const board = Array.from({length: response.boardSize}, () =>
            Array.from({length: response.boardSize}, () => ' ')
        );

        if (response.board.moves) {
            for (const [position, player] of Object.entries(response.board.moves)) {
                const substring = position.substring(0, 2);

                if (!isNaN(Number(substring))) {
                    const row = parseInt(substring, 10) - 1;
                    const col = position[2].charCodeAt(0) - 'A'.charCodeAt(0);
                    board[row][col] = player as string;
                } else {
                    const row = parseInt(position[0], 10) - 1;
                    const col = position[1].charCodeAt(0) - 'A'.charCodeAt(0);
                    board[row][col] = player as string;
                }
            }
        }

        if (playerB === '') {
            useFetch(`users/${response.playerB}`).then(p => {
                p.json().then(p2 => {
                    if (p.ok) {
                        setPlayerB(p2.properties.username)
                    }
                })
            })
        }

        if (playerW === '') {
            useFetch(`users/${response.playerW}`).then(p => {
                p.json().then(p2 => {
                    if (p.ok) {
                        setPlayerW(p2.properties.username)
                    }
                })
            })
        }
        return board;
    };

    useEffect(() => {
        const period = 1000;
        const tid = setInterval(async () => {
            const rsp = await useFetch(`games/${gameId}`);
            const body = await rsp.json();
            const properties = body.properties;
            if (properties && properties !== response) {
                setResponse(properties);
                if (!played) setPlayed(true);
                if (properties.board.variant.includes("Swap") && Object.keys(properties.board.moves).length === 2
                    && playerBId !== properties.playerB) {
                    const temp = playerB
                    setPlayerB(playerW)
                    setPlayerW(temp)
                    setPlayerBId(properties.playerB)
                }
                checkGameState();
            }
        }, period);
        return () => clearInterval(tid);
    }, [played, gameId, response]);

    useEffect(() => {
        if (win !== '') {
            alert(win);
        }
    }, [win]);

    useEffect(() => {
        fetchGame();
    }, []);

    const renderBoard = () => {
        const board = generateBoard();

        return (
            <div>
                {forfeit && (
                    <Navigate to={"/"}/>
                )}
                {playable && (
                    <CreateButton onClick={handleForfeit} label={"Forfeit"}/>
                )}

                {response && response.board.variant.includes("Swap") && Object.keys(response.board.moves).length === 1
                    && playerId === response.playerW && (
                        <CreateButton onClick={() => setSwapping(!swapping)} label={!swapping ? "Swap" : "Unswap"}/>
                    )}
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <table style={{borderCollapse: 'collapse', border: '1px solid black', backgroundColor: '#D2B48C'}}>
                        <tbody>
                        {board.map((row, rowIndex) => (
                            <tr key={rowIndex}>
                                {row.map((cell, colIndex) => (
                                    <td
                                        key={colIndex}
                                        style={{
                                            background: 'linear-gradient(to bottom, transparent 47%, #000 47%, #000 53%,  transparent 53%), linear-gradient(to right, transparent 47%, #000 47%, #000 53%, transparent 53%)',
                                            border: 'none',
                                            width: '40px',
                                            height: '40px',
                                            textAlign: 'center',
                                        }}
                                    >
                                        {cell === ' ' && playable && (
                                            <button
                                                style={{
                                                    width: '70%',
                                                    height: '70%',
                                                    cursor: 'pointer',
                                                    backgroundColor: rowIndex == Math.floor(response.boardSize / 2) && colIndex == Math.floor(response.boardSize / 2) ? 'red' : 'rgba(255, 255, 255, 0.0)',
                                                    border: 'none'
                                                }}
                                                onClick={() => play(rowIndex, colIndex)}
                                            >
                                            </button>
                                        )}
                                        {cell !== ' ' && (
                                            <img src={cell === 'B' ? blackstone : whitestone}
                                                 style={{width: '90%', height: '90%'}}/>
                                        )}
                                    </td>
                                ))}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    };

    return (
        <div style={fontStyle}>
            <Navbar/>
            {response === null && (
                <h1>Loading</h1>
            )}
            {response && (
                <div>
                    <h2>Game Details:</h2>
                    <p>Rules - {response.board.rules}</p>
                    <p>Variant - {response.board.variant}</p>
                    <h4>
                        <p>{`Black pieces - ${playerB}`}</p>
                        <p>{`White pieces - ${playerW}`}</p>
                        {response.board.turn === 'B' ? <p>Turn: Black</p> : <p>Turn: White</p>}
                    </h4>
                    {renderBoard()}
                </div>
            )}
        </div>
    );
}

export {GetGame};