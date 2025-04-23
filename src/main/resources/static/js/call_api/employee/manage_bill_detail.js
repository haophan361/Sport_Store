function fetchOrderDetail() {
    const bill_id = document.getElementById("bill-id").textContent
    fetch("/employee/getBillDetail?bill_id=" + encodeURIComponent(bill_id))
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            renderBillDetail(data)
        })
}

function renderBillDetail(data) {
    const tbody = document.getElementById("orderDetail-list");
    tbody.innerHTML = '';

    let grandTotal = 0;

    data.forEach(item => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${item.bill_detail_id}</td>
            <td>${item.option_id}</td>
            <td>${item.product_name}</td>
            <td>${item.color}</td>
            <td>${item.size}</td>
            <td>${item.stock}</td>
            <td class="text-right">${item.price.toLocaleString() + ".000 VNĐ"}</td>
            <td class="text-center">${item.quantity}</td>
            <td class="text-right">${item.total_price.toLocaleString() + ".000 VNĐ"}</td>
        `;
        tbody.appendChild(tr);

        grandTotal += Number(item.total_price);
    });

    const totalCell = document.getElementById("order-total");
    if (totalCell) {
        totalCell.textContent = grandTotal.toLocaleString() + ".000 VNĐ"
    }
}

function employee_confirmOrder(bill_id) {
    bootbox.confirm(
        {
            title: "Xác nhận đơn hàng",
            message: "Bạn có muốn xác nhận đơn hàng này?",
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
                    apiRequest("/employee/confirm_bill?bill_id=" + encodeURIComponent(bill_id), "PUT", {}, null,
                        "/employee/manageOrder", null, "include")
                }
            }
        })
}

function employee_cancelOrder(bill_id) {
    bootbox.confirm(
        {
            title: "Hủy đơn hàng",
            message: "Bạn có muốn hủy đơn hàng này?",
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
                    apiRequest("/employee/cancel_bill?bill_id=" + encodeURIComponent(bill_id), "PUT", {}, null,
                        "/employee/manageOrder", null, "include")
                }
            }
        })
}

document.addEventListener("DOMContentLoaded", function () {
    fetchOrderDetail()
})