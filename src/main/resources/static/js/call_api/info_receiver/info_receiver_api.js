function form_addInfo_Receiver() {
    document.getElementById("addReceiverModalLabel").innerText = "Thêm thông tin nhận hàng";
    document.getElementById("receiver_name").value = "";
    document.getElementById("receiver_phone").value = "";
    document.getElementById("cityName").value = "";
    document.getElementById("districtName").value = "";
    document.getElementById("wardName").value = "";
    document.getElementById("street").value = "";
    document.getElementById("cities").length = 1;
    document.getElementById("districts").length = 1;
    document.getElementById("wards").length = 1;
    document.getElementById("add_button").style.display = "block";
    document.getElementById("update_button").style.display = "none";

    const modal = new bootstrap.Modal(document.getElementById('addReceiverModal'));
    modal.show();
    document.getElementById('addReceiverModal').addEventListener('shown.bs.modal', function () {
        setTimeout(function () {
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.style.overflow = 'auto';
        }, 5);
    });
}

function add_infoReceiver() {
    const form_infoReceiver =
        {
            name: document.getElementById("receiver_name").value,
            phone: document.getElementById("receiver_phone").value,
            city: document.getElementById("cityName").value,
            district: document.getElementById("districtName").value,
            ward: document.getElementById("wardName").value,
            street: document.getElementById("street").value
        }
    apiRequest("/customer/add_infoReceiver", "POST", {'Content-type': 'application/json'},
        JSON.stringify(form_infoReceiver), null, null, "include",
        function () {
            window.location.reload();
        })
}

function form_updateInfo_Receiver(receiver_id, name, phone, city, district, ward, street) {
    document.getElementById("addReceiverModalLabel").innerText = "Cập nhật thông tin nhận hàng";
    document.getElementById("receiver_id").value = receiver_id;
    document.getElementById("receiver_name").value = name;
    document.getElementById("receiver_phone").value = phone;
    document.getElementById("cityName").value = city;
    document.getElementById("districtName").value = district;
    document.getElementById("wardName").value = ward;
    document.getElementById("street").value = street;
    document.getElementById("add_button").style.display = "none";
    document.getElementById("update_button").style.display = "block";
    const modal = new bootstrap.Modal(document.getElementById('addReceiverModal'));
    modal.show();
    document.getElementById('addReceiverModal').addEventListener('shown.bs.modal', function () {
        setTimeout(function () {
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.style.overflow = 'auto';
        }, 5);
    });
}


function update_infoReceiver() {
    const form_infoReceiver =
        {
            receiver_id: document.getElementById("receiver_id").value,
            name: document.getElementById("receiver_name").value,
            phone: document.getElementById("receiver_phone").value,
            city: document.getElementById("cityName").value,
            district: document.getElementById("districtName").value,
            ward: document.getElementById("wardName").value,
            street: document.getElementById("street").value
        }
    apiRequest("/customer/update_infoReceiver", "POST", {'Content-type': 'application/json'},
        JSON.stringify(form_infoReceiver), null, null, "include",
        function () {
            window.location.reload();
        })
}

function delete_infoReceiver(receiver_id) {
    bootbox.confirm(
        {
            title: "Xác nhận xóa thông tin",
            message: "Bạn có muốn xóa thông tin nhận hàng này không",
            buttons:
                {
                    confirm:
                        {
                            label: 'Xác nhận',
                            className: 'btn-success'
                        },
                    cancel:
                        {
                            label: 'Không',
                            className: 'btn-dark'
                        }
                },
            callback: function (result) {
                if (result) {
                    apiRequest("/customer/delete_infoReceiver", "POST", {'Content-type': 'application/json'},
                        receiver_id, null, null, "include",
                        function () {
                            window.location.reload();
                        })
                }
            }
        })
}