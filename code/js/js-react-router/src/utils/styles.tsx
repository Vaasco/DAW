import background from './images/background.png'

const fontFamily = 'Nova Square, sans-serif'

const fontStyle = {
    fontFamily:fontFamily
}

const containerStyle: React.CSSProperties = {
    background: `url(${background})`,
    backgroundSize: "cover",
    backgroundRepeat: "no-repeat",
    minHeight: "100vh",
};

const navbarStyle = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '10px',
    backgroundColor: 'white',
    fontFamily: fontFamily,
};

const linkContainerStyle = {
    display: 'flex',
    alignItems: 'center',
};

const linkStyle = {
    textDecoration: 'none',
    color: 'black',
    marginRight: '10px',
};

const playButtonStyle = {
    background: 'none',
    border: 'none',
    cursor: 'pointer',
};

const buttonStyle = {
    background: '#D3D3D3',
    fontSize: '16px',
    borderRadius: '5px',
    cursor: 'pointer',
    border: '1px solid black',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.3)'
}

const inputStyle = {
    backgroundColor: '#D3D3D3',
    fontSize: '16px',
    border: '1px solid black',
    outline: 'none'
}

export {fontStyle, playButtonStyle, linkStyle, linkContainerStyle, navbarStyle, buttonStyle, inputStyle, containerStyle}