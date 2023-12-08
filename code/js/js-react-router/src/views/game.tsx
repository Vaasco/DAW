import React, {useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navbar} from "../utils/navBar";

//const blackstone = require('../../../../jvm/demo/src/main/kotlin/com/example/demo/pieces/blackstone.png').default;


export function GetGame() {
    const [id, setId] = useState('');
    const [response, setResponse] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await useFetch(`games/${id}`)
            const body = await response.json()
            const properties = body.properties
            console.log("this is response", response)
            setResponse(properties)
        } catch (error) {
            console.error('Error fetching game:', error.message);
            setResponse(null);
        }
    };

    const play = async (rowIndex, colIndex) => {
        const fetch = await  useFetch(`games/${id}`,'POST',)

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
                                {cell === ' ' && response.state === 'Playing' && (
                                    <button
                                        style={{
                                            width: '100%',
                                            height: '100%',
                                            cursor: 'pointer',
                                        }}
                                        onClick={() => play(rowIndex, colIndex)}
                                    >
                                        {/* Pode colocar aqui o conteúdo do botão, como um ícone ou texto */}

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
                    <div>
                        <h3>Board:</h3>
                        {renderBoard()}
                    </div>
                </div>
            )}
        </div>
    );
}