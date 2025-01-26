function logout() {
    fetch("/logout",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include"
        })
        .then(response => {
            if (response.ok) {
                bootbox.alert(
                    {
                        title: "Thông báo",
                        message: " Đăng xuất thành công",
                        backdrop: true,
                        callback: function () {
                            window.location.href = "/";
                        }
                    }
                )
            } else {
                bootbox.alert(
                    {
                        title: "Thông báo",
                        message: "Đăng xuất không thành công",
                        backdrop: true,
                    }
                )
            }
        })
}