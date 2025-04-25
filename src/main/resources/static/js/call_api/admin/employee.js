$(document).ready(function () {
    fetchEmployees();

    // Validation form thêm nhân viên
    $('#employee-form').on('submit', function (e) {
        e.preventDefault();
    });

    // Xử lý hiển thị validation khi mở modal
    $('#addEmployeeModal').on('show.bs.modal', function () {
        $('#employee-form')[0].reset();
        $('#employee-form').find('.is-invalid').removeClass('is-invalid');
        $('#employee-form').find('.invalid-feedback').hide();
    });
});

function fetchEmployees() {
    $.ajax({
        url: '/admin/employees',
        method: 'GET',
        success: function (response) {
            if (response.message) {
                $('#employee-list').html('<tr><td colspan="8" class="text-center">Không có nhân viên nào</td></tr>');
                return;
            }
            renderEmployees(response);
        },
        error: function (xhr, status, error) {
            $('#employee-list').html('<tr><td colspan="8" class="text-center text-danger">Lỗi khi tải danh sách nhân viên</td></tr>');
        }
    });
}

function activateAccount(email) {
    bootbox.confirm(
        {
            title: "Xác nhận",
            message: "Bạn có muốn kích hoạt tài khoản này không?",
            buttons:
                {
                    confirm:
                        {
                            label: 'Xác nhận',
                        },
                    cancel:
                        {
                            label: 'Hủy',
                        }
                },
            callback: function (result) {
                if (result) {
                    apiRequest("/admin/activateAccount?email=" + email, "PUT", {},
                        null, null, null, "include", function () {
                            fetchEmployees()
                        })
                }
            }
        })
}

function deactivateAccount(email) {
    bootbox.confirm(
        {
            title: "Xác nhận",
            message: "Bạn có muốn khóa tài khoản này không?",
            buttons:
                {
                    confirm:
                        {
                            label: 'Xác nhận',
                        },
                    cancel:
                        {
                            label: 'Hủy',
                        }
                },
            callback: function (result) {
                if (result) {
                    apiRequest("/admin/deactivateAccount?email=" + email, "PUT", {},
                        null, null, null, "include", function () {
                            fetchEmployees()
                        })
                }
            }
        })
}

function renderEmployees(employees) {
    const tbody = $('#employee-list');
    tbody.empty();
    if ($.fn.DataTable.isDataTable('#employeeTable')) {
        $('#employeeTable').DataTable().clear().destroy();
    }
    employees.forEach(e => {
        const row = `
            <tr>
                <td>${e.name}</td>
                <td>${e.email}</td>
                <td>${e.phone}</td>
                <td>${e.dateOfBirth}</td>
                <td>${e.address}</td>
                <td>${e.gender ? 'Nam' : 'Nữ'}</td>
                <td>${e.online ? '<span class="badge badge-success">Online</span>' : '<span class="badge badge-secondary">Offline</span>'}</td>
                <td>${e.active ? '<span class="badge badge-primary">Đã kích hoạt</span>' : '<span class="badge badge-danger">Bị vô hiệu hóa</span>'}</td>
                <td>${e.active ? `<button class="btn btn-danger" onclick="deactivateAccount('${e.email}')">Khóa tài khoản</button>` 
                                : `<button class="btn btn-primary" onclick="activateAccount('${e.email}')">Kích hoạt</button>`}</td>
            </tr>
        `;
        tbody.append(row);
    });
    $('#employeeTable').DataTable({
        pageLength: 10,
        language: {
            search: "Tìm kiếm:",
            lengthMenu: "Hiển thị _MENU_ bản ghi mỗi trang",
            info: "Trang _PAGE_ trong tổng số _PAGES_ trang",
            paginate: {
                previous: "Trước",
                next: "Sau"
            }
        }
    });
}

function saveEmployee() {
    const form = $('#employee-form');

    // Kiểm tra validation
    if (!form[0].checkValidity()) {
        form.find(':invalid').each(function () {
            $(this).addClass('is-invalid');
            $(this).siblings('.invalid-feedback').show();
        });
        return;
    }

    const newEmployee = {
        employee_name: $('#employee_name').val(),
        employee_address: $('#employee_address').val(),
        employee_phone: $('#employee_phone').val(),
        employee_email: $('#employee_email').val(),
        employee_date_of_birth: $('#employee_date_of_birth').val(),
        employee_gender: $('#employee_gender').val() === 'true',
        password: $('#password').val()
    };

    $.ajax({
        url: '/admin/employees',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(newEmployee),
        success: function (response) {
            $('#addEmployeeModal').modal('hide');
            form[0].reset();
            fetchEmployees();
            // Hiển thị thông báo thành công
            bootbox.alert({
                title: "Thông báo",
                message: response.message || 'Tạo nhân viên thành công!'
            });
        },
        error: function (xhr, status, error) {
            // Hiển thị thông báo lỗi
            let errorMsg = 'Lỗi khi tạo nhân viên!';
            if (xhr.responseText) {
                errorMsg = xhr.responseText;
            }

            bootbox.alert({
                title: "Báo Lỗi",
                message: errorMsg
            });
        }
    });
}