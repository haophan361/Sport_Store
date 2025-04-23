// ==================== XEM CHI TIẾT PHIẾU NHẬP ====================
function loadBillDetailSupply(bill_supply_id) {
    fetchBillSupplyDetail(bill_supply_id)
        .then(bill_supply_details => {
            if (!Array.isArray(bill_supply_details)) {
                bill_supply_details = [];
            }
            if ($.fn.DataTable.isDataTable('#billSupplyDetailTable')) {
                $('#billSupplyDetailTable').DataTable().clear().destroy();
            }
            document.getElementById("selected-bill-id").textContent = bill_supply_id;

            const tbody = document.getElementById("bill-detail-body");
            tbody.innerHTML = "";
            let total_bill = 0;
            bill_supply_details.forEach(d => {
                const total = d.quantity * d.cost;
                total_bill = total_bill + total
                tbody.innerHTML += `
                <tr>
                    <td>${d.detail_id}</td>
                    <td>${d.product_name}</td>
                    <td>${d.option_id}</td>
                    <td>${d.color}</td>
                    <td>${d.size}</td>
                    <td>${d.quantity}</td>
                    <td>${formatCurrency(d.cost)}</td>
                    <td>${formatCurrency(total)}</td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-outline-danger btn-delete-detail" data-id="${d.detail_id}">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>
                `;
            });
            document.getElementById("nhapkho-content").style.display = "none";
            document.getElementById("bill-detail-section").style.display = "block";
            document.getElementById("detail-total-amount").textContent = formatCurrency(total_bill);

            $('#billSupplyDetailTable').DataTable({
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
        })
}

function fetchBillSupplyDetail(bill_supply_id) {
    return fetch("/admin/getBillSupplyDetail?bill_supply_id=" + encodeURIComponent(bill_supply_id))
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
}

// ==================== SỰ KIỆN NHẤN "XEM" ====================
$(document).on("click", ".btn-view-detail", function () {
    const bill_supply_id = $(this).data("id");
    loadBillDetailSupply(bill_supply_id);
});