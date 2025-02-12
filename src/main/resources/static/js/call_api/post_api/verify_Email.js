function getTimeLeft(startTime, duration) {
    if (!startTime) {
        return 0;
    }
    let elapsed = Date.now() - parseInt(startTime);
    return Math.max(0, Math.floor((duration - elapsed) / 1000));
}

document.addEventListener("DOMContentLoaded", function () {
    const inputs = document.querySelectorAll(".code-email-input");
    const countdownVerify = document.getElementById("countdownVerify");
    const resendButton = document.getElementById("resendCode");


    if (localStorage.getItem("verify_startTime") === null) {
        localStorage.setItem("verify_startTime", Date.now());
    }

    if (localStorage.getItem("resend_startTime") === null) {
        localStorage.setItem("resend_startTime", Date.now());
    }

    const VERIFY_DURATION = 300 * 1000;
    const RESEND_DURATION = 30 * 1000;

    let verifyStartTime = localStorage.getItem("verify_startTime");
    let resendStartTime = localStorage.getItem("resend_startTime");

    let verifyTimeLeft = getTimeLeft(verifyStartTime, VERIFY_DURATION);
    let resendTimeLeft = getTimeLeft(resendStartTime, RESEND_DURATION);

    function updateCountdown() {
        let minute = Math.floor(verifyTimeLeft / 60);
        let second = verifyTimeLeft % 60;
        if (second < 10) {
            countdownVerify.innerText = `(${minute}:0${second})`;
        } else {
            countdownVerify.innerText = `(${minute}:${second})`;
        }

        if (verifyTimeLeft > 0) {
            verifyTimeLeft--;
        } else {
            clearInterval(timer);
            countdownVerify.innerText = "Mã đã hết hạn";
        }
    }

    function updateResendCountdown() {
        let resendCountdown = document.getElementById("resendCountdown");
        resendCountdown.innerText = `${resendTimeLeft}`;
        if (resendTimeLeft > 0) {
            resendTimeLeft--;
        } else {
            clearInterval(resendTimer);
            resendButton.classList.remove("disabled");
            resendButton.innerHTML = "Gửi lại";
        }
    }

    let timer, resendTimer;
    if (verifyTimeLeft > 0) {
        timer = setInterval(updateCountdown, 1000);
    } else {
        countdownVerify.innerText = "Mã đã hết hạn";
    }

    if (resendTimeLeft > 0) {
        resendButton.classList.add("disabled");
        resendButton.innerHTML = `Gửi lại sau (<span id="resendCountdown">${resendTimeLeft}</span>s)`;
        resendTimer = setInterval(updateResendCountdown, 1000);
    } else {
        resendButton.classList.remove("disabled");
    }

    resendButton.addEventListener("click", function (e) {
        e.preventDefault();
        if (!resendButton.classList.contains("disabled")) {
            localStorage.setItem("resend_startTime", Date.now());
            resendTimeLeft = RESEND_DURATION / 1000;
            resendButton.classList.add("disabled");
            resendButton.innerHTML = `Gửi lại (<span id="resendCountdown">${resendTimeLeft}</span>s)`;
            resendTimer = setInterval(updateResendCountdown, 1000);
            fetch("/web/resendCode_VerifyEmail", {
                method: "POST",
                headers: {
                    'Content-type': 'application/json'
                },
                credentials: "include"
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
                        localStorage.setItem("verify_startTime", Date.now());
                        timer = setInterval(updateCountdown, 1000);
                        window.location.reload()
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
    });
    inputs.forEach((input, index) => {
        input.addEventListener("input", (e) => {
            let value = e.target.value;
            if (/^[A-Za-z0-9]$/.test(value)) {
                e.target.value = value;
                if (index < inputs.length - 1) {
                    inputs[index + 1].focus();
                }
            } else {
                e.target.value = "";
            }
        });

        input.addEventListener("keydown", (e) => {
            if (e.key === "Backspace" && !input.value && index > 0) {
                inputs[index - 1].focus();
            }
        });
    });

});

function checkCodeResetPassword() {
    let input = document.querySelectorAll(".code-email-input")
    let code = "";
    input.forEach(inputField => {
        code += inputField.value
    })
    if (code === "") {
        bootbox.alert({
            title: "Thông báo",
            message: "Vui lòng nhập mã xác nhận",
            backdrop: true,
        })
        return
    }
    fetch("/web/checkCode_ForgetPassword", {
        method: "POST",
        headers: {
            'Content-type': 'text/plain',
        },
        credentials: "include",
        body: code
    }).then(response => {
        if (!response.ok) {
            return response.text().then(error => {
                throw new Error(error)
            })
        }
        return response.json()
    }).then(data => {
        bootbox.alert({
            title: "Thông báo",
            message: data.message,
            backdrop: true,
            callback: function () {
                localStorage.removeItem("verify_startTime");
                localStorage.removeItem("resend_startTime");
                if (data.redirectUrl) {
                    window.location.href = data.redirectUrl
                }
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