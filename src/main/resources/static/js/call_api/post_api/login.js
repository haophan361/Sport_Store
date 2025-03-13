function login() {
    const formLogin =
        {
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        }
    apiRequest("/login", "POST", {"Content-Type": "application/json"}, JSON.stringify(formLogin)
        , "/", null, "include")
}
