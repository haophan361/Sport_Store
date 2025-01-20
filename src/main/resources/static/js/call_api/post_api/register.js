document.addEventListener('DOMContentLoaded', function()
{
    flatpickr("#date_of_birth",
        {
            maxDate:"today",
            enableTime: false,
            dateFormat: "Y-m-d",
        });
});

function register()
{
    const formRegister=
        {
            name:document.getElementById("name").value,
            date_of_birth:document.getElementById("date_of_birth").value,
            gender: document.querySelector('input[name="gender"]:checked')?.value || null,
            phone:document.getElementById("phone").value,
            email:document.getElementById("email").value,
            password:document.getElementById("password").value,
            confirmPassword:document.getElementById("confirmPassword").value
        }
    if (formRegister.password!==formRegister.confirmPassword)
    {
        bootbox.alert(
            {
                title: "Thông báo",
                message:"Mật khẩu và xác nhận mật khẩu không trùng khớp vui lòng nhập lại.",
                backdrop:true
            }
        )
        return
    }
    fetch("/register",
        {
            method:"POST",
            headers:{
                'Content-type': 'application/json',
            },
            body: JSON.stringify(formRegister)
        })
    .then(response =>
    {
        if (response.ok)
        {
            return response.text();
        }
        else
        {
            return response.text().then(text => { throw new Error(text); });
        }
    })
    .then(message =>
    {
        bootbox.alert({
            title: "Thông báo",
            message: message,
            backdrop: true,
            callback: function ()
            {
                window.location.href = "/web/form_login";
            }
        });
    })
    .catch(error =>
    {
        bootbox.alert({
            title: "Thông báo lỗi",
            message: error.message,
            backdrop: true
        });
    });
}
