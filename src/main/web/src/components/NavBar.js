import React from 'react';
import {Button, ButtonGroup} from "@mui/material";
import './NavBar.scss';

const NavBar = ( props ) => {

    const { pages, selectedPage, setSelectedPage } = props;

    const handleSelectPage = key =>
    {
        setSelectedPage(key);
    }

    return (
        <div className="navbar">
            <div className="navbar-content">
                <ButtonGroup variant="text" aria-label="text button group">
                {
                    pages.map
                    ( page =>
                        <Button
                            key={ page.key + '-button'}
                            onClick={ () => handleSelectPage(page.key) }
                            className={ selectedPage === page.key ? 'focused' : '' }
                        >
                            { page.title }
                        </Button>
                    )
                }
                </ButtonGroup>

            </div>
            <div className="login-action">

            </div>
        </div>
    );
}

export default NavBar;