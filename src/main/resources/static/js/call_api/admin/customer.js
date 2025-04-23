function fetchCustomers() {
    $.ajax({
        url: '/admin/customers',
        method: 'GET',
        success: function (response) {
            if (response.message) {
                $('#customer-list').html('<tr><td colspan="6" class="text-center">Không có khách hàng nào</td></tr>');
                return;
            }

            renderCustomers(response);
        },
        error: function (xhr, status, error) {
            $('#customer-list').html('<tr><td colspan="6" class="text-center text-danger">Lỗi khi tải danh sách khách hàng</td></tr>');
        }
    });
}

function renderCustomers(customers) {
    const tbody = $('#customer-list');
    tbody.empty();
    if ($.fn.DataTable.isDataTable('#customerTable')) {
        $('#customerTable').DataTable().clear().destroy();
    }
    customers.forEach(customer => {
        const row = `
                <tr>
                    <td>${customer.name}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone || 'N/A'}</td>
                    <td>${customer.dateOfBirth || 'N/A'}</td>
                    <td>${customer.online ? '<span class="badge badge-success">Online</span>' : '<span class="badge badge-secondary">Offline</span>'}</td>
                    <td>${customer.active ? '<span class="badge badge-primary">Đã kích hoạt</span>' : '<span class="badge badge-danger">Bị vô hiệu hóa</span>'}</td>
                </tr>
            `;
        tbody.append(row);
    });
    $('#customerTable').DataTable({
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


$(document).ready(function () {
    fetchCustomers();
});