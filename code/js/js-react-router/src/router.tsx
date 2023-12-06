import {fetchReq} from "./utils/fetchReq";
import {Home} from "./views/home";
import {GetStats, statsLoader} from "./views/statistics";
import {CreateLobby} from "./views/lobby";
import {GetGame} from "./views/game";
import {Authors, authorsLoader} from "./views/authors";
import {Login} from "./views/login";
import {Sign} from "./views/signin";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import {createRoot} from "react-dom/client";
import React from "react";
import {ErrorComp} from "./Errors/ErrorLogin";


const router = createBrowserRouter(
    [
        {
            path: "/",
            element: < Home/>,
        },
        {
            path: "/authors",
            element: <Authors/>,
            loader: authorsLoader
        },
        {
            path: "/login",
            element: <Login/>
        },
        {
            path: "/sign",
            element: <Sign/>
        },
        {
            path: "/play",
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
        }
    ]
)

export function main() {
    const root = createRoot(document.getElementById("container"))
    root.render(
        <RouterProvider router={router}/>
    );

}