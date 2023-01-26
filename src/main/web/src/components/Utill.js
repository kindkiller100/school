
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
        'http://localhost:8080/' + url,
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
}