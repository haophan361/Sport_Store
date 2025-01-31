function FirstLoginMessage() {
    const firstLoginMessage = getCookie("firstLogin_WithGoogle");
    if (firstLoginMessage) {
        bootbox.alert({
            title: "Thông báo",
            message: "Đây là lần đầu bạn đăng nhập bằng Google, mật khẩu cho tài khoản sẽ được tạo tự động và gửi vào Gmail của bạn."
        })
        deleteCookie("firstLogin_WithGoogle")
    }
}

document.addEventListener("DOMContentLoaded", function () {
    FirstLoginMessage();
})
