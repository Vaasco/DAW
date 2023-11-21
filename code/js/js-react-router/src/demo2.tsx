import React from 'react'
import {createRoot} from 'react-dom/client'
import {
    createBrowserRouter,
    RouterProvider,
    Link,
    Outlet
} from "react-router-dom";


const router = createBrowserRouter(
    [
        {
            path: "/",
            element: < Home/>,
        },
        {
            path: "/",
            element: < Home/>,
        },
    ]
)

function Home() {
    return (
        <div>
            <h1>Home</h1>

        </div>
    );
}


export function demo() {
    const root = createRoot(document.getElementById("container"))
    root.render(<RouterProvider router={router}/>)
} 