import React, {useEffect, useState, useRef} from "react";
import { fetchWithoutAuth } from './Utill.js';

const Subjects = (props) =>
{
    const [data, setData] = useState([]);

    const calledOnce = useRef( false );

    useEffect(
        () =>
        {
            if( ! calledOnce.current )
            {
                calledOnce.current = true;
                fetchWithoutAuth('subjects' )
                    .then
                    (
                        response =>
                            setData(response || [] )
                    )
                    .catch( console.log )
            }
        },
        []
    )

    return (
        <div>
            Subjects
        </div>
    )
}

export default Subjects;