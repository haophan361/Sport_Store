function apiRequest(url, method, headers, body = null, success_redirectUrl,
                    failed_redirectUrl, credentials, onSuccess = null, onFailed = null) {
    const options = {
        method: method,
        headers: headers,
        credentials: credentials
    };
    if (body) options.body = body;

    fetch(url, options)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
        })
        .then(message => {
            bootbox.alert({
                title: "Thông báo",
                message: message.message,
                backdrop: true,
                callback: function () {
                    if (onSuccess) onSuccess()
                    if (success_redirectUrl) window.location.href = success_redirectUrl;
                }
            });
        })
        .catch(error => {
            bootbox.alert({
                title: "Thông báo lỗi",
                message: error.message,
                backdrop: true,
                callback: function () {
                    if (onFailed) onFailed()
                    if (failed_redirectUrl) window.location.href = failed_redirectUrl
                }
            });
        });
}