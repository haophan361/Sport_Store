function filterOrders() {
    const status = document.getElementById("orderStatusFilter").value
    fetchBill(status)
}

function fetchBill(status = null) {
    fetch("/employee/getAllBill?type_status=" + encodeURIComponent(status))
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            renderBill(data)
        })
}

function renderBill(data) {
    const tbody = document.getElementById("orders-list");
    tbody.innerHTML = '';
    if ($.fn.DataTable.isDataTable('#ordersTable')) {
        $('#ordersTable').DataTable().clear().destroy();
    }
    data.forEach(bill => {
        let statusHTML = '';
        if (bill.active && bill.confirmation_date == null) {
            statusHTML = '<span class="order-status status-pending">Chưa xác nhận</span>';
        } else if (bill.active && bill.confirmation_date != null && bill.receive_date == null) {
            statusHTML = '<span class="order-status status-confirmed">Đã xác nhận</span>';
        } else if (!bill.active && bill.employee_email != null) {
            statusHTML = '<span class="order-status status-canceled-staff">Đơn bị hủy bởi nhân viên</span>';
        } else if (!bill.active && bill.employee_email == null) {
            statusHTML = '<span class="order-status status-canceled-customer">Đơn bị hủy bởi khách hàng</span>';
        } else if (bill.receive_date != null) {
            statusHTML = '<span class="order-status status-success">Hoàn thành</span>';
        }

        const tr = document.createElement('tr');
        tr.dataset.billId = bill.bill_id;
        tr.innerHTML = `
            <td>${bill.bill_id}</td>
            <td>${bill.total_amount.toLocaleString() + ".000 VNĐ"}</td>
            <td>${bill.purchase_date}</td>
            <td>${bill.confirmation_date ? bill.confirmation_date : ''}</td>
            <td>${bill.paid ? 'Đã thanh toán' : 'Chưa thanh toán'}</td>
            <td>${bill.receive_date ? bill.receive_date : ''}</td>
            <td>${statusHTML}</td>
            <td>${bill.customer_email}</td>
            <td>${bill.employee_email || ''}</td>
        `;

        tr.addEventListener('click', function () {
            console.log(bill.bill_id)
            window.location.href = "/employee/orderDetail?bill_id=" + encodeURIComponent(bill.bill_id)
        });

        tbody.appendChild(tr);
    });

    $('#ordersTable').DataTable({
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

document.addEventListener("DOMContentLoaded", function () {
    fetchBill()
})