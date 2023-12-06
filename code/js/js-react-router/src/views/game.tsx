import React, { useState } from "react";
import { fetchReq } from "../utils/fetchReq";
import {Navbar} from "../utils/navBar";

export function GetGame() {
    const [id, setId] = useState('');
    const [response, setResponse] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetchReq(`games/${id}`)
            console.log("this is response", response)
            setResponse(response)
        } catch (error) {
            console.error('Error fetching game:', error.message);
            setResponse(null);
        }
    };

    const generateBoard = () => {
        const board = Array.from({ length: response.boardSize }, () =>
            Array.from({ length: response.boardSize }, () => ' ')
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

    const renderBoard = () => {
        const board = generateBoard();

        return (
            <table style={{ borderCollapse: 'collapse', border: '1px solid black' }}>
                <tbody>
                {board.map((row, rowIndex) => (
                    <tr key={rowIndex}>
                        {row.map((cell, colIndex) => (
                            <td
                                key={colIndex}
                                style={{
                                    border: '1px solid black',
                                    width: '40px', // Adjust the width as needed
                                    height: '40px', // Adjust the height as needed
                                    textAlign: 'center',
                                }}
                            >
                                {cell}
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
            <Navbar />
            <form onSubmit={handleSubmit}>
                <label>
                    Game ID:
                    <input
                        type="text"
                        value={id}
                        onChange={(e) => setId(e.target.value)}
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
                    <p>Turn: {response.board.turn}</p>
                    <p>State: {response.state}</p>
                    <p>Player B: {response.playerB}</p>
                    <p>Player W: {response.playerW}</p>
                    <p>Board Size: {response.boardSize}</p>
                    <div>
                        <h3>Board:</h3>
                        {renderBoard()}
                    </div>
                </div>
            )}
        </div>
    );
}
