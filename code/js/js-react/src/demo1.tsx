import React, { useState } from 'react';
import { createRoot } from 'react-dom/client';

function Comp4(state) {
    return (
        <div>
            <p>Comp4</p>
            <p>{state.state.toString()}</p>
        </div>
    );
}


function Comp3(state) {
    return (
        <div>
            <p>Comp3</p>
            <Comp4 state={state.state}/>
        </div>
    );
}
function Comp2(state) {
    return (
        <div>
            <p>Comp2</p>
            <Comp3 state={state.state} />
        </div>
    );
}

function Comp1() {
    const [value, setValue] = useState(false);
    return (
        <div>
            <p>Comp1</p>
            <Comp2 state={value}/>
            <button type="button" onClick={() => setValue(!value)}>
                Click
            </button>
        </div>
    );
}

export function demo() {
    const root = createRoot(document.getElementById('container'));
    root.render(<Comp1 />);
}
