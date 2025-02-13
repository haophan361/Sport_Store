function changePassword() {
    const form_changePassword = {
        new_password: document.getElementById("new_password").value,
        confirm_password: document.getElementById("confirm_password").value,
        old_password: document.getElementById("old_password").value
    }
    fetch("/user/changePassword", {
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