function getLink_forgetPassword() {
    const email = document.getElementById("email").value
    if (!email.trim()) {
        bootbox.alert({
            title: "Thông báo",
            message: "Vui lòng nhập vào email",
            backdrop: true
        })
        return
    }
    document.getElementById("loading-overlay").style.display = "flex";
    const token_resetPassword = getCookie("token_resetPassword")
    if (token_resetPassword !== undefined) {
        deleteCookie("token_resetPassword")
    }
    fetch(`/web/sendLink_ForgetPassword?email=${encodeURIComponent(email)}`, {
        method: "GET",
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
            backdrop: true
        })
    }).catch(error => {
        document.getElementById("loading-overlay").style.display = "none";
        bootbox.alert({
            title: "Lỗi",
            message: error.message,
            backdrop: true
        });
    });
}

let checkToken_resetPassword_interval = 5000;
const interval_CheckTokenResetPassword = setInterval(() => {
    const token_resetPassword = getCookie("token_resetPassword")
    if (token_resetPassword !== undefined) {
        fetch("/web/check_token_resetPassword", {
            method: "GET",
            credentials: "include"
        }).then(response => {
            if (response.ok) {
                clearInterval(interval_CheckTokenResetPassword)
                window.location.href = "/form/changePassword"
            }
        })
    }
}, checkToken_resetPassword_interval)