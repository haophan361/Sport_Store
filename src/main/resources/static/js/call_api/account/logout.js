function logout() {
    apiRequest("/logout", "POST", {}, null,
        "/", null, "include")
}