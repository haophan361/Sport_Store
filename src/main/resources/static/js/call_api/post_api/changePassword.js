document.addEventListener("DOMContentLoaded", function () {
    const token_resetPassword = getCookie("token_resetPassword")
    if (token_resetPassword !== undefined) {
        document.getElementById("input_oldPassword").hidden = true
    }
})


function changePassword() {
    const token_resetPassword = getCookie("token_resetPassword")
    const form_changePassword = {
        new_password: document.getElementById("new_password").value,
        confirm_password: document.getElementById("confirm_password").value,
    }
    let url;
    if (token_resetPassword !== undefined) {
        url = "/web/forgetPassword"
    } else {
        url = "/user/changePassword"
        form_changePassword.old_password = document.getElementById("old_password").value
    }
    fetch(url, {
        method: "POST",
        headers:
            {
                'Content-type': 'application/json'
            },
        body: JSON.stringify(form_changePassword),
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