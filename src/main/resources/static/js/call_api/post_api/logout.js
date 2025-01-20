function logout()
{
    fetch("/logout",
        {
            method: "POST",
            headers:{
                "Content-Type": "application/json",
                Authorization: "Bearer "+localStorage.getItem("token")
            },
        })
        .then(response =>
        {
            if (response.ok)
            {
                bootbox.alert(
                    {
                        title:"Thông báo",
                        message:" Đăng xuất thành công",
                        backdrop: true,
                        callback: function ()
                        {
                            localStorage.clear()
                            sessionStorage.clear()
                            window.location.href = "/";
                        }
                    }
                )
            }
            else
            {
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