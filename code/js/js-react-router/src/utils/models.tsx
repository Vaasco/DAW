import {buttonStyle, inputStyle} from "./styles";
import React from "react";

// In utils/models.ts or wherever CreateButton is defined
type CreateButtonProps = {
    onClick: () => void;
    label: string;
    type?: "button" | "submit" | "reset";
};

type CreateInputProps = {
    label?: string;
    display?: string;
    id?: string;
    name?: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
};

type CreateRadioProps = {
    name: string,
    checked: boolean,
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const CreateButton: React.FC<CreateButtonProps> = ({ onClick, label,type }) => {
    return (
        <button style={buttonStyle} type={type} onClick={onClick}>
            {label}
        </button>
    );
};

const CreateStringInput: React.FC<CreateInputProps> = ({label, display, id, name, value, onChange}) => {
    return (
        <div>
            <label htmlFor={label}> {display}</label>
            <input style={inputStyle}
                   id={id}
                   type="text"
                   name={name}
                   value={value}
                   onChange={onChange}
            />
        </div>
    );
}

const CreateRadioInput: React.FC<CreateRadioProps> = ({name, checked, onChange}) => {
    return (
        <label>
            <input
                type="radio"
                value={name}
                checked={checked}
                onChange={onChange}
            />
            {name}
        </label>
    )
}

export {CreateButton, CreateStringInput, CreateRadioInput}