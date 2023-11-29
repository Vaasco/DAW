import React, {useState} from 'react'
import {createRoot} from 'react-dom/client'

function useCounter(initial) {
    const [counter, setCounter] = useState(initial)

    const increment = () => setCounter(counter + 1)

    const decrement = () => setCounter(counter - 1)

    return [counter, increment, decrement]
}


function Counter(props) {

    return (
        <div>
            <p>
                <button onClick={props.add}>+</button>
            </p>
            <p>{props.value}</p>
            <p>
                <button onClick={props.sub}>-</button>
            </p>
        </div>
    )
}

function TwoCounters() {

    const [counter, increment, decrement] = useCounter(0)

    return (
        <div>
            <Counter value={counter} add={increment} sub={decrement}/>
            <Counter value={counter} add={increment} sub={decrement}/>
        </div>
    )
}

export function demo() {
    const root = createRoot(document.getElementById("container"))
    root.render(<TwoCounters/>)
}