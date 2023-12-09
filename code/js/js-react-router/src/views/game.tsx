import React, {useEffect, useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navbar} from "../utils/navBar";
import toastr from 'toastr'
import {fontStyle} from "../utils/styles";
import blackstone from "../utils/images/blackstone.png"
import whitestone from "../utils/images/whitestone.png"
import {useParams} from "react-router-dom";

export function GetGame() {
    const [gameId, setGameId] = useState('');
    const [response, setResponse] = useState(null);
    const [playerId, setPlayerId] = useState('');
    const [submitting, setSubmitting] = useState(false);
    const [played, setPlayed] = useState(false);
    const [playerB, setPlayerB] = useState('');
    const [playerW, setPlayerW] = useState('');
    const [win, setWin] = useState('');

    const fetchGame = async () => {
        if (response === null) {
            const rsp = await useFetch(`games/${gameId}`)
            const body = await rsp.json()
            if (rsp.ok) {
                setResponse(body.properties)
            }
            if (!rsp.ok) {
                handleError(body.error)
            }
        }
    }

    useEffect(() => {
        const id = document.cookie.replace(/.*id\s*=\s*([^;]*).*/, "$1")
        setPlayerId(id)
    }, []);

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

    const handleError = (error) => {
        toastr.options = {
            positionClass: 'toast-top',
            progressBar: true,
            closeButton: true,
            preventDuplicates: true,
            timeOut: 5000,
            extendedTimeOut: 1000,
            iconClass: 'custom-error-icon',
            onHidden: () => setSubmitting(false)
        }
        toastr.error(error)
    }

    const playable = response && response.state === 'Playing' &&
        ((playerId == response.playerB && response.board.turn == 'B') ||
            (playerId == response.playerW && response.board.turn == 'W'))

    const play = async (rowIndex: number, colIndex: number) => {
        const swap = response.board.variant == 'Swap' ? 1 : null
        const requestBody = {
            row: rowIndex,
            col: colIndex,
            swap: swap
        }

        const rsp = await useFetch(`games/${gameId}`, 'POST', requestBody)
        const body = await rsp.json()
        const properties = body.properties
        if (!rsp.ok) {
            handleError(body.error)
        } else {
            setResponse(properties)
            setSubmitting(true)
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
        return board;
    };

    useEffect(() => {
        const period = 2000;
        const tid = setInterval(async () => {
            const rsp2 = await useFetch(`games/${gameId}`);
            const body2 = await rsp2.json();
            if (body2.properties) {
                if (body2.properties.board.moves !== response.board.moves) {
                    setResponse(body2.properties);
                    if(!played) setPlayed(true);
                    checkGameState();
                }
            }
        }, period);
        return () => clearInterval(tid);
    }, [played, gameId, response]);

    useEffect(() => {
        if (win !== '') {
            alert(win);
        }
    }, [submitting, played]);

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
                                        border: '1px solid black',
                                        width: '40px',
                                        height: '40px',
                                        textAlign: 'center',
                                    }}
                                >
                                    {cell === ' ' && playable && (
                                        <button
                                            style={{
                                                width: '100%',
                                                height: '100%',
                                                cursor: 'pointer',
                                                backgroundColor: rowIndex === 7 && colIndex === 7 ? 'red' : 'rgba(255, 255, 255, 0.0)',
                                                border: 'none'
                                            }}
                                            onClick={() => play(rowIndex, colIndex)}
                                        >
                                        </button>
                                    )}
                                    {cell !== ' ' && (
                                        <img src={cell === 'B' ? blackstone : whitestone}
                                             style={{width: '100%', height: '100%'}}/>
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
            {response && (
                <div>
                    <h2>Game Details</h2>
                    <p>ID: {response.id}</p>
                    <p>Rules: {response.board.rules}</p>
                    <p>Variant: {response.board.variant}</p>
                    {renderBoard()}
                </div>
            )}
        </div>
    );
}
