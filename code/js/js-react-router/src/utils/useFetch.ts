import {
    useState,
    useEffect,
} from 'react'

const BASE_URL = "http://localhost:8081/api/"

/*
export type fetchRsp = {
    response: any,
    error: any
}
*/

/*export function useFetch(uri: string, method: string = "GET", bodyRequest: any = null) : fetchRsp {
    console.log("inside useFetch")
    const [content, setContent] = useState(undefined)
    const [error, setError] = useState(undefined)

    useEffect(() =>{
        let cancelled = false
        async function doFetch(){
            console.log("inside doFetch")
            try {
                console.log("inside try")
                const rsp = await fetch(BASE_URL + uri, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(bodyRequest)
                })
                console.log("response headers", rsp.headers.get("content-type"))
                if(!rsp.ok) {
                    const contentType = rsp.headers.get("content-type");
                    if(contentType && contentType.indexOf("application/problem+json") !== -1) {
                        const bodyError = await rsp.text();
                        console.log("bodyError", bodyError)
                        setError(bodyError);
                        return;
                    }
                }
                const bodyResponse = await rsp.json()
                console.log("bodyResponse", bodyResponse)
                if(cancelled) return
                setContent(bodyResponse)
            } catch (err) {
                console.log("Error in doFetch", err)
            }
        }
        setContent(undefined)
        if(!content && !error) doFetch()
        return ()=>{
            console.log("CleanUp")
            cancelled=true
        }
    }, [uri, method, bodyRequest])
    return { response: content, error: error }
}*/

export function useFetch(path:string, method:string = "GET", body?:object) {
    const options = {
        method: method,
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
        },
        ...(body && {body: JSON.stringify(body)}),
    };
    return fetch(BASE_URL + path, options);
}