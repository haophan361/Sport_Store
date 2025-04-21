// employee.js
$(document).ready(function() {
    fetchEmployees();

    // Validation form thêm nhân viên
    $('#employee-form').on('submit', function(e) {
        e.preventDefault();
    });

    // Xử lý hiển thị validation khi mở modal
    $('#addEmployeeModal').on('show.bs.modal', function() {
        $('#employee-form')[0].reset();
        $('#employee-form').find('.is-invalid').removeClass('is-invalid');
        $('#employee-form').find('.invalid-feedback').hide();
    });
});

function fetchEmployees() {
    apiRequest(
        '/admin/employees',
        'GET',
        { 'Content-Type': 'application/json' },
        null,
        null,
        null,
        'include',
        function(response) {
            if (response.message) {
                $('#employee-list').html('<tr><td colspan="8" class="text-center">Không có nhân viên nào</td></tr>');
                return;
            }
            renderEmployees(response);
        },
        function() {
            bootbox.alert({
                title: "Thông báo lỗi",
                message: "Lỗi khi tải danh sách nhân viên",
                backdrop: true
            });
            $('#employee-list').html('<tr><td colspan="8" class="text-center text-danger">Lỗi khi tải danh sách nhân viên</td></tr>');
        }
    );
}

function renderEmployees(employees) {
    const tbody = $('#employee-list');
    tbody.empty();
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
            </tr>
        `;
        tbody.append(row);
    });
}

function searchEmployee() {
    const keyword = $('#employee-search-keyword').val().toLowerCase().trim();
    apiRequest(
        '/admin/employees',
        'GET',
        { 'Content-Type': 'application/json' },
        null,
        null,
        null,
        'include',
        function(response) {
            if (response.message) {
                $('#employee-list').html('<tr><td colspan="8" class="text-center">Không có nhân viên nào</td></tr>');
                return;
            }
            if (!keyword) {
                renderEmployees(response);
                return;
            }
            const filteredEmployees = response.filter(e =>
                (e.name && e.name.toLowerCase().includes(keyword)) ||
                (e.email && e.email.toLowerCase().includes(keyword)) ||
                (e.phone && e.phone.toLowerCase().includes(keyword))
            );
            renderEmployees(filteredEmployees);
            if (filteredEmployees.length === 0) {
                $('#employee-list').html('<tr><td colspan="8" class="text-center">Không tìm thấy nhân viên nào</td></tr>');
            }
        },
        function() {
            bootbox.alert({
                title: "Thông báo lỗi",
                message: "Lỗi khi tìm kiếm nhân viên",
                backdrop: true
            });
            $('#employee-list').html('<tr><td colspan="8" class="text-center text-danger">Lỗi khi tìm kiếm nhân viên</td></tr>');
        }
    );
}

function saveEmployee() {
    const form = $('#employee-form');

    // Kiểm tra validation
    if (!form[0].checkValidity()) {
        form.find(':invalid').each(function() {
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

    apiRequest(
        '/admin/employees',
        'POST',
        { 'Content-Type': 'application/json' },
        JSON.stringify(newEmployee),
        null,
        null,
        'include',
        function(response) {
            $('#addEmployeeModal').modal('hide');
            form[0].reset();
            fetchEmployees();
            bootbox.alert({
                title: "Thông báo",
                message: response.message || "Tạo nhân viên thành công!",
                backdrop: true
            });
        },
        function(error) {
            let errorMsg = error.message || "Lỗi khi tạo nhân viên!";
            bootbox.alert({
                title: "Thông báo lỗi",
                message: errorMsg,
                backdrop: true
            });
        }
    );
}