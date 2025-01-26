function login() {
    const formLogin =
        {
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        }
    fetch("/login",
        {
            method: "POST",
            headers: {
                'Content-type': 'application/json',
            },
            body: JSON.stringify(formLogin)
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
        })
        .then(data => {
            bootbox.alert({
                title: "Thông báo",
                message: "Đăng nhập thành công",
                backdrop: true,
                callback: function () {
                    window.location.href = "/";
                }
            });
        })
        .catch(error => {
            bootbox.alert({
                title: "Thông báo lỗi",
                message: error.message,
                backdrop: true
            });
        });
}
