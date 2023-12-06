import {useEffect} from "react";
import {fetchReq} from "./fetchReq";

function waitGame() {
    useEffect(() => {
        const getCookieValue = (name) => {
            const cookies = document.cookie.split("; ");
            for (const cookie of cookies) {
                const [cookieName, cookieValue] = cookie.split("=");
                if (cookieName === name) {
                    return cookieValue;
                }
            }
            return null;
        };


        const username = getCookieValue("username");

        const fetchGameData = async => {

        }

        if (username) {
            const url = `http://localhost:8081/api/games/user/${username}`;
            const response = fetchReq(url)
            if (response != null) {

            }
            console.log(username);
            console.log(url);
        }
    }, []);
}

export default waitGame;
