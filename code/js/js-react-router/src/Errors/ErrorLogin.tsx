import {useRouteError} from "react-router-dom";
import React from "react";

export function ErrorComp({error} : {error : string}) {
    return (
        <div>
            <h1>Oops! Error during login</h1>
            <p>Sorry, an unexpected error has occurred.</p>
            <p>
                <i>{error}</i>
            </p>
        </div>
    );
}




