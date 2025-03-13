function getCode_VerifyEmail() {
    const email = document.getElementById("email").value
    if (!email.trim()) {
        bootbox.alert({
            title: "Thông báo",
            message: "Vui lòng nhập vào email",
            backdrop: true
        })
        return
    }
    apiRequest("/web/sendCode_VerifyEmail_forgetPassword", "POST",
        {}, email, "/form/check_codeVerifyEmail", null, "include", callback_remove_time_forgetPassword)
}

function callback_remove_time_forgetPassword() {
    if (localStorage.getItem("verify_startTime") !== null) {
        localStorage.removeItem("verify_startTime")
    }
    sessionStorage.setItem("typeVerify", "forgetPassword")
}