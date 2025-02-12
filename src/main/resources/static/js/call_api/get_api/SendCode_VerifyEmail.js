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
    document.getElementById("loading-overlay").style.display = "flex";
    fetch(`/web/sendCode_VerifyEmail_forgetPassword?email=${encodeURIComponent(email)}`, {
        method: "POST",
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
                if (localStorage.getItem("verify_startTime") !== null) {
                    localStorage.removeItem("verify_startTime")
                }
                sessionStorage.setItem("typeVerify", "forgetPassword")
                window.location.href = "/form/check_codeVerifyEmail"
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