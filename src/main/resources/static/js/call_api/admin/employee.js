function fetchEmployees() {
    $.ajax({
        url: '/admin/employees',
        method: 'GET',
        success: function(response) {
            if (response.message) {
                $('#employee-list').html('<tr><td colspan="6" class="text-center">Không có nhân viên nào</td></tr>');
                return;
            }

            renderEmployees(response);
        },
        error: function(xhr, status, error) {
            $('#employee-list').html('<tr><td colspan="6" class="text-center text-danger">Lỗi khi tải danh sách nhân viên</td></tr>');
        }
    });
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
                    <td>${e.active ? '<span class="badge badge-primary">Hoạt động</span>' : '<span class="badge badge-danger">Không hoạt động</span>'}</td>
                </tr>
            `;
        tbody.append(row);
    });
}

function searchEmployee() {
    const keyword = $('#employee-search-keyword').val().toLowerCase().trim();

    $.ajax({
        url: '/admin/employees',
        method: 'GET',
        cache: false,
        success: function(response) {
            if (response.message) {
                $('#employee-list').html('<tr><td colspan="6" class="text-center">Không có nhân viên nào</td></tr>');
                return;
            }

            // Nếu không có từ khóa, hiển thị toàn bộ danh sách
            if (!keyword) {
                renderEmployees(response);
                return;
            }

            // Lọc danh sách khách hàng
            const filteredEmployees = response.filter(e =>
                (e.name && e.name.toLowerCase().includes(keyword)) ||
                (e.email && e.email.toLowerCase().includes(keyword)) ||
                (e.phone && e.phone.toLowerCase().includes(keyword))
            );

            // Render danh sách đã lọc
            renderEmployees(filteredEmployees);

            // Hiển thị thông báo nếu không tìm thấy
            if (filteredEmployees.length === 0) {
                $('#employee-list').html('<tr><td colspan="6" class="text-center">Không tìm thấy nhân viên nào</td></tr>');
            }
        },
        error: function(xhr, status, error) {
            $('#employee-list').html('<tr><td colspan="6" class="text-center text-danger">Lỗi khi tìm kiếm nhân viên</td></tr>');
        }
    });
}

$(document).ready(function() {
    fetchEmployees();
});