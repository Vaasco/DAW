import {Home} from "./views/home";
import {GetStats, statsLoader} from "./views/statistics";
import {CreateLobby} from "./views/lobby";
import {GetGame} from "./views/game";
import {Authors} from "./views/authors";
import {Login} from "./views/login";
import {SignUp} from "./views/signup";
import {GetGameById} from "./views/getgame";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import {createRoot} from "react-dom/client";
import React from "react";
import {Logout} from "./views/logout";
import {AuthContainer} from "./utils/AuthContainer";

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
            element: <SignUp/>
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
            path: "/games/:id",
            element: <GetGame/>
        },
        {
            path: "/logout",
            element: <Logout/>
        },
        {
            path: "/games",
            element: <GetGameById/>
        }
    ]
)

function App() {
    return (
        <AuthContainer>
            <RouterProvider router={router}/>
        </AuthContainer>
    );
}

export function main() {
    const root = createRoot(document.getElementById("container"))
    root.render(
        <App/>
    );
}