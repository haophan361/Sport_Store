function changePassword() {
    const form_changePassword = {
        new_password: document.getElementById("new_password").value,
        confirm_password: document.getElementById("confirm_password").value,
        old_password: document.getElementById("old_password").value
    }
    apiRequest("/user/changePassword", "PUT", {'Content-type': 'application/json'}, JSON.stringify(form_changePassword)
        , "/", null, "include")
}