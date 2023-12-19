import React, {useState} from "react";
import {useFetch} from "../utils/useFetch";
import {Navbar} from "../utils/navBar";
import {fontStyle} from "../utils/styles";
import {CreateButton, CreateStringInput} from "../utils/models";
import {errorHandler} from "../utils/errorHandler";
import {Navigate} from "react-router-dom";


export function GetGameById() {
    const [gameId, setGameId] = useState('');
    const [username, setUsername] = useState('');
    const [searchGame, setSearchGame] = useState(false);
    const handleGameIdSubmit = async (e) => {
        e.preventDefault();
        setSearchGame(true)

    };

    const handleUsernameSubmit = async (e) => {
        e.preventDefault();
        const rsp = await useFetch(`games/user/${username}`, "GET")
        const body = await rsp.json()

       if (!rsp.ok) {
           errorHandler(body.error)
       }
       if (rsp.ok) {
           setGameId(body.properties.id)
           setSearchGame(true)
       }
    };

    return (
        <div style={fontStyle}>
            <Navbar/>
            {searchGame && gameId && <Navigate to={`/games/${gameId}`} replace={true}/>}
            <div>
                <form onSubmit={handleGameIdSubmit}>
                    <div>
                        Search for a game:
                    </div>
                    <div>
                        <CreateStringInput
                            placeholder={"Game id"}
                            type={"text"}
                            value={gameId}
                            onChange={(e)=> setGameId(e.target.value)}
                        />
                        <CreateButton onClick={()=>{}} label={"Search"} type={"submit"} />
                    </div>
                </form>
                <form onSubmit={handleUsernameSubmit}>
                    <div>
                        <CreateStringInput
                            placeholder={"Username"}
                            type={"text"}
                            value={username}
                            onChange={(e)=> setUsername(e.target.value)}
                        />
                        <CreateButton onClick={()=>{}} label={"Search"} type={"submit"} />
                    </div>
                </form>
            </div>
        </div>
    )
}