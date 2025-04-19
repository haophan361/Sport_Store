function fetchCustomers() {
    $.ajax({
        url: '/admin/customers',
        method: 'GET',
        success: function(response) {
            if (response.message) {
                $('#customer-list').html('<tr><td colspan="6" class="text-center">Không có khách hàng nào</td></tr>');
                return;
            }

            renderCustomers(response);
        },
        error: function(xhr, status, error) {
            $('#customer-list').html('<tr><td colspan="6" class="text-center text-danger">Lỗi khi tải danh sách khách hàng</td></tr>');
        }
    });
}

function renderCustomers(customers) {
    const tbody = $('#customer-list');
    tbody.empty();
    customers.forEach(customer => {
        const row = `
                <tr>
                    <td>${customer.name}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone || 'N/A'}</td>
                    <td>${customer.dateOfBirth || 'N/A'}</td>
                    <td>${customer.online ? '<span class="badge badge-success">Online</span>' : '<span class="badge badge-secondary">Offline</span>'}</td>
                    <td>${customer.active ? '<span class="badge badge-primary">Hoạt động</span>' : '<span class="badge badge-danger">Không hoạt động</span>'}</td>
                </tr>
            `;
        tbody.append(row);
    });
}

function searchCustomer() {
    const keyword = $('#customer-search-keyword').val().toLowerCase().trim();

    $.ajax({
        url: '/admin/customers',
        method: 'GET',
        cache: false,
        success: function(response) {
            if (response.message) {
                $('#customer-list').html('<tr><td colspan="6" class="text-center">Không có khách hàng nào</td></tr>');
                return;
            }

            // Nếu không có từ khóa, hiển thị toàn bộ danh sách
            if (!keyword) {
                renderCustomers(response);
                return;
            }

            // Lọc danh sách khách hàng
            const filteredCustomers = response.filter(customer =>
                (customer.name && customer.name.toLowerCase().includes(keyword)) ||
                (customer.email && customer.email.toLowerCase().includes(keyword)) ||
                (customer.phone && customer.phone.toLowerCase().includes(keyword))
            );

            // Render danh sách đã lọc
            renderCustomers(filteredCustomers);

            // Hiển thị thông báo nếu không tìm thấy
            if (filteredCustomers.length === 0) {
                $('#customer-list').html('<tr><td colspan="6" class="text-center">Không tìm thấy khách hàng nào</td></tr>');
            }
        },
        error: function(xhr, status, error) {
            $('#customer-list').html('<tr><td colspan="6" class="text-center text-danger">Lỗi khi tìm kiếm khách hàng</td></tr>');
        }
    });
}

$(document).ready(function() {
    fetchCustomers();
});