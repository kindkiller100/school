import React, { useState } from 'react';
import {StyledEngineProvider} from '@mui/material/styles';

import Subjects from './components/Subjects';
import NavBar from './components/NavBar';
import './App.scss';

export const PAGES = [
    {
        key: 'subjects',
        title: 'Предметы'
    },
    {
        key: 'students',
        title: 'Студенты'
    },
    {
        key: 'lessons',
        title: 'Занятия'
    },
    {
        key: 'teachers',
        title: 'Преподаватели'
    },
    {
        key: 'payments',
        title: 'Платежи'
    },
];

const App = () => {

    const [ selectedPage, setSelectedPage ] = useState( PAGES[0].key );

    return (
        <StyledEngineProvider injectFirst>
            <div className="app-wrapper">
                <NavBar
                    pages={PAGES}
                    selectedPage={selectedPage}
                    setSelectedPage={setSelectedPage}
                />
                {
                    selectedPage === 'subjects' && <Subjects/>
                }
            </div>
        </StyledEngineProvider>
    );
}

export default App;
