let refresh_interval = 300000;
let inactivity_timeout = 900000;

let last_activityTime = Date.now();

function resetLastActivityTime() {
    last_activityTime = Date.now();
}

function callback_logout() {
    bootbox.alert(
        {
            title: "Thông báo",
            message: "Bạn đã bị đăng xuất do không hoạt động không một khoảng thời gian",
            backdrop: true,
            callback: function () {
                window.location.href = "/";
            }
        }
    )
}

window.addEventListener("mousemove", resetLastActivityTime);
window.addEventListener("click", resetLastActivityTime);
window.addEventListener("scroll", resetLastActivityTime);
window.addEventListener("keydown", resetLastActivityTime);
setInterval(() => {
    const current_time = Date.now()
    checkIsLoggedIn().then(isLoggedIn => {
        if (isLoggedIn) {
            if (current_time - last_activityTime < inactivity_timeout) {
                refreshToken()
            } else {
                apiRequest("/logout", "POST", {}, null, null, null,
                    "include", callback_logout, null)
            }
        }
    })
}, refresh_interval)

function refreshToken() {
    fetch("/refresh",
        {
            method: "POST",
            credentials: "include"
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Không thể gia hạn token. Bạn sẽ bị đăng xuất.");
            }
            console.log("Token đã được gia hạn thành công.");
        })
        .catch(error => {
            console.error(error);
            bootbox.alert({
                title: "Thông báo",
                message: "Có lỗi xảy ra khi gia hạn token. Bạn sẽ bị đăng xuất.",
                backdrop: true,
                callback: function () {
                    logout()
                }
            });
        });
}

function checkIsLoggedIn() {
    return fetch("/check_login", {
        method: "GET",
        credentials: "include"
    })
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => {
            return false;
        });
}