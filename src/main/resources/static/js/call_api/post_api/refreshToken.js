let refresh_interval = 300000;
let inactivity_timeout = 900000;
let last_activityTime = Date.now();

function resetLastActivityTime() {
    last_activityTime = Date.now();
}

window.addEventListener("mousemove", resetLastActivityTime);
window.addEventListener("click", resetLastActivityTime);
window.addEventListener("scroll", resetLastActivityTime);
window.addEventListener("keydown", resetLastActivityTime);

setInterval(() => {
    const current_time = Date.now()
    if (current_time - last_activityTime < inactivity_timeout) {
        if (localStorage.getItem("token") !== null) {
            refreshToken()
        }
    } else {
        localStorage.removeItem("token")
        bootbox.alert(
            {
                title: "Thông báo",
                message: "Bạn đã bị đăng xuất do không hoạt động trong một khoảng thời gian",
                backdrop: true,
                callback: function () {
                    logout()
                }
            })
    }
}, refresh_interval)

function refreshToken() {
    fetch("/refresh",
        {
            method: "POST",
            headers:
                {
                    "Content-Type": "application/json",
                    // Authorization: "Bearer " + localStorage.getItem("token")
                },
            credentials: "include"
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Không thể gia hạn token. Bạn sẽ bị đăng xuất.");
            }
            return response.json();
        })
        .then(data => {
            if (data.refresh_token) {
                // localStorage.setItem("token", data.refresh_token);
                console.log("Token đã được gia hạn thành công.");
            } else {
                throw new Error("Phản hồi không hợp lệ từ server.");
            }
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