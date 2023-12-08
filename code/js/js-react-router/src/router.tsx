import {useFetch} from "./utils/useFetch";
import {Home} from "./views/home";
import {GetStats, statsLoader} from "./views/statistics";
import {CreateLobby} from "./views/lobby";
import {GetGame} from "./views/game";
import {Authors} from "./views/authors";
import {Login} from "./views/login";
import {SignIn} from "./views/signin";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import {createRoot} from "react-dom/client";
import React from "react";
import {ErrorComp} from "./Errors/ErrorLogin";


const router = createBrowserRouter(
    [
        {
            path: "/",
            element: <Home/>,
        },
        {
            path: "/authors",
            element: <Authors/>
        },
        {
            path: "/login",
            element: <Login/>
        },
        {
            path: "/sign",
            element: <SignIn/>
        },
        {
            path: "/lobby",
            element: <CreateLobby/>
        },
        {
            path: "/stats",
            element: <GetStats/>,
            loader: statsLoader
        },
        {
            path: "/game",
            element: <GetGame/>
        },
        /*{
            path: "/play",
            element: <PlayGame/>
        }*/
    ]
)

export function main() {
    const root = createRoot(document.getElementById("container"))
    root.render(
        <RouterProvider router={router}/>
    );

}