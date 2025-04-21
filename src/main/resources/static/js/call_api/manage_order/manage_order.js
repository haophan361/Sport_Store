let ordersTable;

$(document).ready(function() {
    // Khởi tạo DataTable
    ordersTable = $('#ordersTable').DataTable({
        data: fakeOrders,
        columns: [
            { data: 'bill_id' },
            { 
                data: 'bill_total_cost',
                render: function(data) {
                    return new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND'
                    }).format(data);
                }
            },
            { 
                data: 'bill_purchase_date',
                render: function(data) {
                    return data ? new Date(data).toLocaleDateString('vi-VN') : '';
                }
            },
            { 
                data: 'bill_confirm_date',
                render: function(data) {
                    return data ? new Date(data).toLocaleDateString('vi-VN') : '';
                }
            },
            { 
                data: 'bill_status_payment',
                render: function(data) {
                    return data === 1 ? 
                        '<span class="badge badge-success">Đã thanh toán</span>' : 
                        '<span class="badge badge-warning">Chưa thanh toán</span>';
                }
            },
            { 
                data: 'bill_receive_date',
                render: function(data) {
                    return data ? new Date(data).toLocaleDateString('vi-VN') : '';
                }
            },
            { 
                data: 'is_active',
                render: function(data) {
                    return data === 1 ? 
                        '<span class="badge badge-success">Hoạt động</span>' : 
                        '<span class="badge badge-danger">Đã hủy</span>';
                }
            },
            { data: 'receiver_id' },
            { data: 'employee_id' },
            { data: 'coupon_id' }
        ]
    });

    // Thêm sự kiện click cho các hàng trong bảng
    $('#ordersTable tbody').on('click', 'tr', function() {
        const data = ordersTable.row(this).data();
        if(data) {
            window.location.href = `/employee/orderDetail/${data.bill_id}`;
        }
});
});
// Hàm lọc đơn hàng
function filterOrders() {
    const status = $('#orderStatusFilter').val();
    if (status) {
        ordersTable.columns(6).search(status).draw();
    } else {
        ordersTable.columns(6).search('').draw();
    }
}