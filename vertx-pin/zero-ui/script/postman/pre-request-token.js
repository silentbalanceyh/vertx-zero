const agent = pm.environment.get("PUB");
const authorizeUrl = agent + "/oauth/authorize";
pm.sendRequest({
    url: authorizeUrl,
    method: 'POST',
    header: 'Content-Type:application/json',
    body: {
        mode: 'raw',
        raw: JSON.stringify({
            "client_id": "8c148b72-7348-4d4e-b604-3b7d6053ac1e",
            "client_secret": "SjLRsomA760GlErdX1cFPP5NfYg0ziQUPDuYQLMZrU2J8eXo",
            "response_type": "code",
            "scope": "vie.app.ox"
        })
    }
}, function (err, response) {
    const code = response.json().data.code;
    pm.environment.set("ACODE", code);
});