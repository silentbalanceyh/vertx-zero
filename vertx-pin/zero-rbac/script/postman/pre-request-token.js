const agent = pm.environment.get("PUB");
const loginUrl = agent + "/oauth/login";
const authorizeUrl = agent + "/oauth/authorize";
const tokenUrl = agent + "/oauth/token";

pm.sendRequest({
    url: loginUrl,
    method: 'POST',
    header: 'Content-Type:application/json',
    body: {
        mode: 'raw',
        raw: JSON.stringify({
            "username": pm.environment.get("V_USER"),
            "password": "E559D4DA17DD1C17BE86FCF49E60E322"
        })
    }
}, function (err, response) {
    const request = response.json().data;

    const authReq = {};
    authReq.client_id = request.key;
    authReq.client_secret = request.clientSecret;
    authReq.response_type = "code";
    authReq.scope = request.scope;

    const raw = JSON.stringify(authReq);
    // 交换 Code
    pm.sendRequest({
        url: authorizeUrl,
        method: 'POST',
        header: 'Content-Type:application/json',
        body: {
            mode: 'raw',
            raw
        }
    }, function(err, response) {
        const code = response.json().data;
        const tokenReq = {};
        tokenReq.client_id = request.key;
        tokenReq.code = code.code;
        // 交换 Token
        pm.sendRequest({
            url: tokenUrl,
            method: 'POST',
            header: 'Content-Type:application/json',
            body: {
                mode: 'raw',
                raw: JSON.stringify(tokenReq)
            }
        }, function(err, response){
            const token = response.json().data.access_token;
            pm.environment.set("V_TOKEN", "Berear " + token);
        })
    })
});