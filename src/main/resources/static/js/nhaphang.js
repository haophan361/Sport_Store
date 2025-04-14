function formatCurrency(number) {
    return number.toLocaleString('vi-VN') + ' đ';
}

// ==================== TẢI DANH SÁCH PHIẾU NHẬP ====================
function loadBills(data = bills) {
    const tbody = document.getElementById("bill-supply-list");
    tbody.innerHTML = "";
    let total = 0;
    data.forEach(b => {
        total += b.bill_supply_cost;
        tbody.innerHTML += `
      <tr>
        <td><a href="#" class="text-primary font-weight-bold">${b.bill_supply_id}</a></td>
        <td>${b.supplier_name}</td>
        <td>${b.supplier_phone}</td>
        <td>${b.supplier_address}</td>
        <td>${b.bill_supply_date}</td>
        <td><span class="badge badge-success">Hoàn thành</span></td>
        <td class="text-right">${formatCurrency(b.bill_supply_cost)}</td>
        <td><button class="btn btn-info btn-sm btn-view-detail" data-id="${b.bill_supply_id}">Xem</button></td>
      </tr>
    `;
    });
    document.getElementById("total-bills").textContent = data.length;
    document.getElementById("total-cost").textContent = formatCurrency(total);
}

// ==================== TÌM KIẾM PHIẾU NHẬP ====================
function searchBill() {
    const keyword = document.getElementById("search-keyword").value.toLowerCase();
    const filtered = bills.filter(b =>
        b.bill_supply_id.toLowerCase().includes(keyword) ||
        b.supplier_name.toLowerCase().includes(keyword)
    );
    loadBills(filtered);
}


// ==================== XEM CHI TIẾT PHIẾU NHẬP ====================
function viewDetail(id) {
    const bill = bills.find(b => b.bill_supply_id === id);
    if (!bill || !bill.details) {
        alert("Không tìm thấy chi tiết hóa đơn.");
        return;
    }

    document.getElementById("selected-bill-id").textContent = bill.bill_supply_id;

    const tbody = document.getElementById("bill-detail-body");
    tbody.innerHTML = "";

    bill.details.forEach(d => {
        const total = d.option_quantity * d.option_cost;
        tbody.innerHTML += `
      <tr>
        <td>${d.detailId ?? '-'}</td>
        <td>${d.product_name}</td>
        <td>${d.option_id}</td>
        <td>${d.option_quantity}</td>
        <td>${formatCurrency(d.option_cost)}</td>
        <td>${formatCurrency(total)}</td>
      </tr>
    `;
    });

    document.getElementById("nhapkho-content").style.display = "none";
    document.getElementById("bill-detail-section").style.display = "block";
}

// ==================== TÌM KIẾM CHI TIẾT ====================
function searchDetailBill() {
    const keyword = document.getElementById("search-bill-detail").value.trim().toLowerCase();
    const rows = document.querySelectorAll("#bill-detail-body tr");

    rows.forEach(row => {
        const productName = row.cells[0].textContent.toLowerCase();
        const optionId = row.cells[1].textContent.toLowerCase();
        row.style.display = productName.includes(keyword) || optionId.includes(keyword) ? "" : "none";
    });
}

// ==================== SỰ KIỆN NHẤN "XEM" ====================
$(document).on("click", ".btn-view-detail", function () {
    const id = $(this).data("id");
    viewDetail(id);
});

// ==================== DOMContentLoaded ====================
document.addEventListener("DOMContentLoaded", () => {
    loadBills();

    document.getElementById("search-bill-detail").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            searchDetailBill();
        }
    });

    document.getElementById("bill-form").addEventListener("submit", function (e) {
        e.preventDefault();
        const newBill = {
            bill_supply_id: document.getElementById("bill_id").value,
            supplier_name: document.getElementById("supplier_name").value,
            supplier_phone: document.getElementById("supplier_phone").value,
            supplier_address: document.getElementById("supplier_address").value,
            bill_supply_date: document.getElementById("bill_date").value,
            bill_supply_cost: parseInt(document.getElementById("bill_cost").value),
            details: []
        };
        bills.push(newBill);
        loadBills();
        $('#addBillModal').modal('hide');
        this.reset();
        alert("\u0110\u00e3 th\u00eam phi\u1ebfu nh\u1eadp m\u1edbi!");
    });

    document.getElementById("bill-detail-form").addEventListener("submit", function (e) {
        e.preventDefault();
        const billId = document.getElementById("selected-bill-id").textContent;
        const bill = bills.find(b => b.bill_supply_id === billId);
        if (!bill) {
            alert("Vui lòng chọn hóa đơn để thêm chi tiết.");
            return;
        }

        const detail = {
            product_name: document.getElementById("detail_product_name").value,
            option_id: document.getElementById("detail_option_id").value,
            option_quantity: parseInt(document.getElementById("detail_quantity").value),
            option_cost: parseInt(document.getElementById("detail_cost").value)
        };

        bill.details.push(detail);
        viewDetail(billId);
        $('#addBillDetailModal').modal('hide');
        this.reset();
        alert("\u0110\u00e3 th\u00eam chi ti\u1ebft phi\u1ebfu nh\u1eadp!");
    });
});

