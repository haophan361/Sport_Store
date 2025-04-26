function changePassword() {
    const form_changePassword = {
        new_password: document.getElementById("new_password").value,
        confirm_password: document.getElementById("confirm_password").value,
        old_password: document.getElementById("old_password").value
    }
    let url = "/customer/changePassword"
    if (document.getElementById("role-user").value === "EMPLOYEE") {
        url = "/employee/changePassword"
    } else if (document.getElementById("role-user").value === "ADMIN") {
        url = "/admin/changePassword"
    }
    apiRequest(url, "PUT", {'Content-type': 'application/json'}, JSON.stringify(form_changePassword)
        , "/", null, "include")
}