import React, {useState} from "react";
import {fetchReq} from "../utils/fetchReq";

export function GetGame() {
    const [id, setId] = useState('');
    const [response, setResponse] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetchReq(`games/${id}`)
            setResponse(response)
        } catch (error) {
            console.error('Error fetching game:', error.message);
            setResponse(null);
        }
    };

    return (
        <div>
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
                        <h3>Moves:</h3>


                        {Array.isArray(response.board.moves) && response.board.moves.length > 0 && (
                            <ul>
                                {response.board.moves.map((move, index) => (
                                    <li key={index}>{/* Display individual move properties here */}</li>
                                ))}
                            </ul>
                        )}

                    </div>
                </div>
            )}
        </div>
    );
}
