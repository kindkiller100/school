

async function fetchJsonWithoutAuth( url, method = "GET", data = null, headers = null )
{
    const response = await fetchWithoutAuth( url, method, data, headers );
    if ( response.status !== 200 )
    {
        const message = await response.text();
        return Promise.reject( message );
    }

    return await response.json();
}

async function fetchWithoutAuth( url, method = "GET", data = null, headers = null )
{
    const response = await _fetch( url, method, data, headers )

    return response;
}

function _fetch( url, method = "GET", data = null, headers = null )
{
    const contentType = ( headers && headers[ 'Content-Type' ] ) || 'application/json';

    let body = data;
    if ( data && ( contentType === 'application/json' ) )
    {
        body = JSON.stringify( data );
    }

    return fetch
    (
        url,
        {
            method: method,
            headers:
                {
                    "Content-Type": contentType,
                    ...headers
                },
            body
        }
    );
}


export
{
    fetchWithoutAuth,
    fetchJsonWithoutAuth
}