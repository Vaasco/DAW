import React, {useEffect, useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navbar} from "../utils/navBar";
import {useParams} from "react-router-dom";

//const blackstone = require('../../../../jvm/demo/src/main/kotlin/com/example/demo/pieces/blackstone.png').default;

export function GetGame() {
    const [gameId, setGameId] = useState('');
    //const gameId = useParams().id
    const [response, setResponse] = useState(null);
    const [playerId, setPlayerId] = useState('');
    const [submitting, setSubmitting] = useState(false)
    const [played, setPlayed] = useState(false)

    useEffect(() => {
        const id = document.cookie.replace(/.*id\s*=\s*([^;]*).*/, "$1")
        setPlayerId(id)
    }, []);


    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const rsp = await useFetch(`games/${gameId}`)
            const body = await rsp.json()
            const properties = body.properties
            setResponse(properties)
        } catch (error) {
            console.error('Error fetching game:', error.message);
            setResponse(null)
        }
    };

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
        setResponse(properties)
        setSubmitting(true)
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
        if (submitting || !played) {
            const tid = setInterval(async () => {
                console.log("ESTOU À ESPERA")
                const rsp2 = await useFetch(`games/${gameId}`)
                const body2 = await rsp2.json()
                if (body2.properties) {
                    if(body2.properties.board.moves !== response.board.moves) {
                        setResponse(body2.properties)
                        setPlayed(true);
                        setSubmitting(false);
                    }
                }
            }, period);
            return () => clearInterval(tid);
        }
    },[submitting, played]);

    const renderBoard = () => {
        const board = generateBoard();

        return (
            <table style={{borderCollapse: 'collapse', border: '1px solid black'}}>
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
                                            backgroundColor: rowIndex == 7 && colIndex == 7 ? 'red' : 'rgba(255, 255, 255, 0.0)',
                                            border: 'none'
                                        }}
                                        onClick={() => play(rowIndex, colIndex)}
                                    >
                                    </button>
                                )}
                                {cell !== ' ' && (
                                    cell
                                )}
                            </td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        );
    };

    return (
        <div>
            <Navbar/>
            <form onSubmit={handleSubmit}>
                <label>
                    Game ID:
                    <input
                        type="text"
                        value={gameId}
                        onChange={(e) => setGameId(e.target.value)}
                    />
                </label>
                <button type="submit">Submit</button>
            </form>
            {response && (
                <div>
                    <h2>Game Details</h2>
                    <p>ID: {response.id}</p>
                    <p>Rules: {response.board.rules}</p>
                    <p>Variant: {response.board.variant}</p>
                    <div>
                        <h3>Board:</h3>
                        {renderBoard()}
                    </div>
                </div>
            )}
        </div>
    );
}