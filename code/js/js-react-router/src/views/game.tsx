import React, {useContext, useEffect, useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navbar} from "../utils/navBar";
import toastr from 'toastr'
import {fontStyle} from "../utils/styles";
import blackstone from "../utils/images/blackstone.png"
import whitestone from "../utils/images/whitestone.png"
import {useParams} from "react-router-dom";
import {context} from "../utils/AuthContainer";
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
        const player = response.playerB == playerId ? 'B' : 'W'
        if (response.state) {
            if (response.state === 'Ended B' && player == 'B') setWin('You Won')
            if (response.state === 'Ended W' && player == 'W') setWin('You Won')
            if (response.state === 'Ended B' && player == 'W') setWin('You Lost')
            if (response.state === 'Ended W' && player == 'B') setWin('You Lost')
            if (response.state === 'Ended D') setWin('The game ended with a draw')
        }
    }

    const errorHandler = (error) => {
        toastr.options = {
            positionClass: 'toast-top',
            progressBar: true,
            closeButton: true,
            preventDuplicates: true,
            timeOut: 5000,
            extendedTimeOut: 1000,
            iconClass: 'custom-error-icon',
        }
        toastr.error(error)
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

    const generateBoard = () => {
        const board = Array.from({length: response.boardSize}, () =>
            Array.from({length: response.boardSize}, () => ' ')
        );

        if (response.board.moves) {
            for (const [position, player] of Object.entries(response.board.moves)) {
                const [row, col] = position.split('').map((c) =>
                    (c >= '1' && c <= '9') ? parseInt(c, 10) - 1 : c.charCodeAt(0) - 'A'.charCodeAt(0)
                );
                board[row][col] = player as string;
            }
        }

        if (playerB === ''){
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
        const period = 2000;
        const tid = setInterval(async () => {
            const rsp = await useFetch(`games/${gameId}`);
            const body = await rsp.json();
            const properties = body.properties;
            if (properties && properties !== response) {
                setResponse(properties);
                if(!played) setPlayed(true);
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
        );
    };

    return (
        <div style={fontStyle}>
            <Navbar/>
            {response === null && (
                <h1>Loading</h1>
            )}
            {response && response.board.variant.includes("Swap") && Object.keys(response.board.moves).length === 1
                && playerId === response.playerW && (
                    <button onClick={() => setSwapping(true)}>Swap</button>
            )}
            {response && (
                <div>
                    <h2>Game Details</h2>
                    <p>Rules: {response.board.rules}</p>
                    <p>Variant: {response.board.variant}</p>
                    <h4><p>{`Black pieces - ${playerB}`}</p>
                        <p>{`White pieces - ${playerW}`}</p></h4>
                    {renderBoard()}
                </div>
            )}
        </div>
    );
}

export {GetGame};