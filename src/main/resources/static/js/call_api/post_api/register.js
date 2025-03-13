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

    apiRequest("/web/sendCode_VerifyEmail_Register", "POST", {'Content-type': 'application/json'},
        JSON.stringify(formRegister), "/form/check_codeVerifyEmail", null, "include",
        callback_remove_time_register)
}

function callback_remove_time_register() {
    {
        if (localStorage.getItem("verify_startTime") !== null) {
            localStorage.removeItem("verify_startTime")
        }
        sessionStorage.setItem("typeVerify", "register")
    }
}

