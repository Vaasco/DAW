import React, {useEffect, useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navbar} from "../utils/navBar";
import toastr from 'toastr'
import {fontStyle} from "../utils/styles";
import blackstone from "../utils/images/blackstone.png"
import whitestone from "../utils/images/whitestone.png"

export function GetGameById() {
    const [response, setResponse] = useState(null);
    const [playerId, setPlayerId] = useState('');
    const [played, setPlayed] = useState(false);
    const [playerB, setPlayerB] = useState('');
    const [playerW, setPlayerW] = useState('');
    const [gameId, setGameId] = useState('');

    const fetchGame = async () => {
        if (response === null) {
            const rsp = await useFetch(`games/${gameId}`)
            const body = await rsp.json()
            if (rsp.ok) {
                setResponse(body.properties)
            }
            if (!rsp.ok) {
                errorHandler(body.error)
            }
        }
    }

    useEffect(() => {
        const id = document.cookie.replace(/.*id\s*=\s*([^;]*).*/, "$1")
        setPlayerId(id)
    }, []);

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

    const handleSubmit = async (e) => {
        e.preventDefault();
        const rsp = await useFetch(`games/${gameId}`)
        const body = await rsp.json()
        const properties = body.properties
        setResponse(properties)
        if (!rsp.ok) {
            setResponse(null)
            errorHandler(body.error)
        }
    };

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

        if(playerB == '') {
            useFetch(`users/${response.playerB}`).then(p => {
                p.json().then(p2 => {
                    if (p.ok) {
                        setPlayerB(p2.properties.username)
                    }
                })
            })
        }

        if(playerW == '') {
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
            const rsp2 = await useFetch(`games/${gameId}`);
            const body2 = await rsp2.json();
            if (body2.properties) {
                if (body2.properties.board.moves !== response.board.moves) {
                    setResponse(body2.properties);
                    if (!played) setPlayed(true);
                }
            }
        }, period);
        return () => clearInterval(tid);
    }, [played, gameId, response]);

    useEffect(() => {
        if(gameId != '') fetchGame();
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
                                            //onClick={() => play(rowIndex, colIndex)}
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

            <div style={fontStyle}>
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
                        <p>Rules: {response.board.rules}</p>
                        <p>Variant: {response.board.variant}</p>
                        <h4><p>{`Black pieces - ${playerB}`}</p>
                            <p>{`White pieces - ${playerW}`}</p></h4>
                        {renderBoard()}
                    </div>
                )}
            </div>
        </div>
    )
}