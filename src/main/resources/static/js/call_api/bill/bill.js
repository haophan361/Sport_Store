function renderBill(data) {
    if ($.fn.DataTable.isDataTable('#billTable')) {
        $('#billTable').DataTable().clear().destroy();
    }
    const tbody = document.getElementById("bill-list")
    tbody.innerHTML = ''
    data.forEach(b => {
        let statusHTML = '';
        if (b.active && b.confirmation_date == null) {
            statusHTML = '<span class="order-status status-pending">Chưa xác nhận</span>';
        } else if (b.active && b.confirmation_date != null) {
            statusHTML = '<span class="order-status status-confirmed">Đã xác nhận</span>';
        } else if (!b.active && b.employee_id != null) {
            statusHTML = '<span class="order-status status-canceled-staff">Đơn bị hủy bởi nhân viên</span>';
        } else if (!b.active && b.employee_id == null) {
            statusHTML = '<span class="order-status status-canceled-customer">Đơn bị hủy bởi khách hàng</span>';
        } else if (b.receive_date != null) {
            statusHTML = '<span class="order-status status-success">Hoàn thành</span>';
        }

        const paymentStatus = b.status_payment ? 'Đã thanh toán' : 'Chưa thanh toán';
        const confirmation_date = b.confirmation_date ? b.confirmation_date : '';
        const receive_date = b.receive_date ? b.receive_date : '';
        const row = `
        <tr data-bill-id="${b.bill_id}">
            <td>${b.bill_id}</td>
            <td>${b.purchase_date}</td>
            <td>${b.total + '.000 VNĐ'}</td>
            <td>${confirmation_date}</td>
            <td>${receive_date}</td>
            <td>${statusHTML}</td>
            <td>${paymentStatus}</td>
            <td>
                <a href="/customer/bill-detail/${b.bill_id}" class="btn btn-info btn-sm">
                    <i class="fa fa-eye"></i>Xem
                </a>
            </td>
        </tr>
        `
        tbody.insertAdjacentHTML("beforeend", row);
    })
    $('#billTable').DataTable({
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


function fetchBill() {
    fetch("/customer/getBill")
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            renderBill(data)
        })
}

document.addEventListener("DOMContentLoaded", function () {
    fetchBill()
})


