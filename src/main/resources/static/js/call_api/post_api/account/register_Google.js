function FirstLoginMessage() {
    const firstLoginMessage = getCookie("register_Google");
    if (firstLoginMessage) {
        bootbox.alert({
            title: "Thông báo",
            message: "Đây là lần đầu bạn đăng nhập bằng Google, mật khẩu cho tài khoản sẽ được tạo tự động và gửi vào Gmail của bạn."
        })
        deleteCookie("register_Google")
    }
}

document.addEventListener("DOMContentLoaded", function () {
    FirstLoginMessage();
})
