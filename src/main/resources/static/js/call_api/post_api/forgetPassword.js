function forgetPassword() {
    const token_resetPassword = getCookie("token_resetPassword")
    const form_forgetPassword = {
        new_password: document.getElementById("new_password").value,
        confirm_password: document.getElementById("confirm_password").value,
    }
    fetch("/web/forgetPassword", {
        method: "POST",
        headers:
            {
                'Content-type': 'application/json'
            },
        body: JSON.stringify(form_forgetPassword),
        credentials: 'include'
    }).then(response => {
        if (!response.ok) {
            return response.text().then(error => {
                throw new Error(error)
            })
        }
        return response.text()
    }).then(message => {
        bootbox.alert({
            title: "Thông báo",
            message: message,
            backdrop: true,
            callback: function () {
                if (token_resetPassword !== undefined) {
                    deleteCookie("token_resetPassword")
                }
                window.location.href = "/"
            }
        })
    }).catch(error => {
        bootbox.alert({
            title: "Lỗi",
            message: error.message,
            backdrop: true
        });
    });

}