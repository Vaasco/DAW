import {buttonStyle, inputStyle} from "./styles";
import React from "react";

// In utils/models.ts or wherever CreateButton is defined
type CreateButtonProps = {
    onClick: () => void;
    label: string;
    type?: "button" | "submit" | "reset";
    testId?: string
};

type CreateInputProps = {
    placeholder?: string;
    label?: string;
    display?: string;
    id?: string;
    type: string;
    name?: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
};

type CreateRadioProps = {
    name: string,
    checked: boolean,
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const CreateButton: React.FC<CreateButtonProps> = ({ onClick, label,type,testId }) => {
    return (
        <button style={buttonStyle} type={type} onClick={onClick} data-testid={testId}>
            {label}
        </button>
    );
};

const CreateStringInput: React.FC<CreateInputProps> = ({placeholder, label, display, id, type, name, value, onChange}) => {
    return (
        <label htmlFor={label}> {display}
            <input style={inputStyle}
                   id={id}
                   type={type}
                   name={name}
                   value={value}
                   onChange={onChange}
                   placeholder={placeholder}
            />
        </label>
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