function forgetPassword() {
    //const token_resetPassword = getCookie("token_resetPassword")
    const form_forgetPassword = {
        new_password: document.getElementById("new_password").value,
        confirm_password: document.getElementById("confirm_password").value,
    }
    apiRequest("/web/forgetPassword", "POST", {'Content-type': 'application/json'},
        JSON.stringify(form_forgetPassword), "/web/form_login", null, "include")
}