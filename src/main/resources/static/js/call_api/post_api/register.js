document.addEventListener('DOMContentLoaded', function () {
    flatpickr("#date_of_birth",
        {
            maxDate: "today",
            enableTime: false,
            dateFormat: "Y-m-d",
        });
});

function register() {
    const formRegister =
        {
            name: document.getElementById("name").value,
            date_of_birth: document.getElementById("date_of_birth").value,
            gender: document.querySelector('input[name="gender"]:checked')?.value || null,
            phone: document.getElementById("phone").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            confirmPassword: document.getElementById("confirmPassword").value
        }
    if (formRegister.password !== formRegister.confirmPassword) {
        bootbox.alert(
            {
                title: "Thông báo",
                message: "Mật khẩu và xác nhận mật khẩu không trùng khớp vui lòng nhập lại.",
                backdrop: true
            }
        )
        return
    }
    fetch("/web/sendCode_VerifyEmail_Register", {
        method: "POST",
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(formRegister)
    }).then(response => {
        if (!response.ok) {
            return response.text().then(error => {
                throw new Error(error);
            });
        }
        return response.text();
    }).then(message => {
        bootbox.alert({
            title: "Thông báo",
            message: message,
            backdrop: true,
            callback: function () {
                if (localStorage.getItem("verify_startTime") !== null) {
                    localStorage.removeItem("verify_startTime")
                }
                sessionStorage.setItem("typeVerify", "register")
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

