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
        <td><a href="#" onclick="viewDetail('${b.bill_supply_id}'); return false;" class="text-primary font-weight-bold">${b.bill_supply_id}</a></td>
        <td>${b.supplier_name}</td>
        <td>${b.supplier_phone}</td>
        <td>${b.supplier_address}</td>
        <td>${b.bill_supply_date}</td>
        <td><span class="badge badge-success">Hoàn thành</span></td>
        <td class="text-right">${formatCurrency(b.bill_supply_cost)}</td>
        <td class="text-center">
          <a href="#" onclick="editBill('${b.bill_supply_id}'); return false;" class="btn btn-sm btn-outline-primary me-1" title="Chỉnh sửa">
            <i class="bi bi-pencil-square"></i>
          </a>
          <a href="#" onclick="deleteBill('${b.bill_supply_id}'); return false;" class="btn btn-sm btn-outline-danger" title="Xoá">
            <i class="bi bi-trash"></i>
          </a>
        </td>
      </tr>
    `;
    });
    document.getElementById("total-bills").textContent = data.length;
    document.getElementById("total-cost").textContent = formatCurrency(total);
}

// Thêm các hàm xử lý chỉnh sửa và xóa
function editBill(billId) {
    event.stopPropagation();
    const bill = bills.find(b => b.bill_supply_id === billId);
    if (bill) {
        // Điền thông tin vào form
        document.getElementById('bill_id').value = bill.bill_supply_id;
        document.getElementById('bill_id').readOnly = true;
        document.getElementById('supplier_name').value = bill.supplier_name;
        document.getElementById('supplier_phone').value = bill.supplier_phone;
        document.getElementById('supplier_address').value = bill.supplier_address;
        document.getElementById('bill_date').value = bill.bill_supply_date;
        document.getElementById('bill_cost').value = bill.bill_supply_cost;
        
        // Thay đổi tiêu đề và nút submit
        document.querySelector('#addBillModal .modal-title').textContent = 'Chỉnh sửa phiếu nhập';
        document.querySelector('#addBillModal button[type="submit"]').textContent = 'Cập nhật';
        
        $('#addBillModal').modal('show');
    }
}

function deleteBill(billId) {
    event.stopPropagation();
    if (confirm('Bạn có chắc chắn muốn xóa phiếu nhập này?')) {
        const index = bills.findIndex(b => b.bill_supply_id === billId);
        if (index !== -1) {
            bills.splice(index, 1);
            loadBills();
        }
    }
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

    bill.details.forEach((d, index) => {
        const total = d.option_quantity * d.option_cost;
        tbody.innerHTML += `
          <tr>
            <td>${d.detailId ? d.detailId : '-'}</td>
            <td>${d.product_name}</td>
            <td>${d.option_id}</td>
            <td>${d.option_quantity}</td>
            <td>${formatCurrency(d.option_cost)}</td>
            <td>${formatCurrency(total)}</td>
            <td class="text-center">
              <button class="btn btn-sm btn-outline-primary mr-1" onclick="editBillDetail('${bill.bill_supply_id}', ${index})">
                <i class="bi bi-pencil-square"></i>
              </button>
              <button class="btn btn-sm btn-outline-danger" onclick="deleteBillDetail('${bill.bill_supply_id}', ${index})">
                <i class="bi bi-trash"></i>
              </button>
            </td>
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

function editBillDetail(billId, detailIndex) {
    const bill = bills.find(b => b.bill_supply_id === billId);
    if (!bill) return;

    const detail = bill.details[detailIndex];
    document.getElementById("detail_product_name").value = detail.product_name;
    document.getElementById("detail_option_id").value = detail.option_id;
    document.getElementById("detail_quantity").value = detail.option_quantity;
    document.getElementById("detail_cost").value = detail.option_cost;

    // Ghi chú vị trí để cập nhật sau
    document.getElementById("bill-detail-form").dataset.editing = detailIndex;

    $('#addBillDetailModal').modal('show');
}

function deleteBillDetail(billId, detailIndex) {
    const bill = bills.find(b => b.bill_supply_id === billId);
    if (!bill) return;

    if (confirm("Bạn có chắc chắn muốn xóa chi tiết này?")) {
        bill.details.splice(detailIndex, 1);
        viewDetail(billId);
    }
}

// ==================== DOMContentLoaded ====================
document.addEventListener("DOMContentLoaded", () => {
    loadBills();

    document.getElementById("search-bill-detail").addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            searchDetailBill();
        }
    });

    document.getElementById("bill-form")?.addEventListener("submit", function(e) {
        e.preventDefault();
        const formData = {
            bill_supply_id: document.getElementById("bill_id").value,
            supplier_name: document.getElementById("supplier_name").value,
            supplier_phone: document.getElementById("supplier_phone").value,
            supplier_address: document.getElementById("supplier_address").value,
            bill_supply_date: document.getElementById("bill_date").value,
            bill_supply_cost: parseInt(document.getElementById("bill_cost").value),
            details: []
        };

        const existingBill = bills.find(b => b.bill_supply_id === formData.bill_supply_id);
        if (existingBill) {
            // Cập nhật hóa đơn hiện có
            Object.assign(existingBill, formData);
            alert("Đã cập nhật phiếu nhập!");
        } else {
            // Thêm hóa đơn mới
            bills.push(formData);
            alert("Đã thêm phiếu nhập mới!");
        }

        loadBills();
        this.reset();
        document.getElementById("bill_id").readOnly = false;
        $('#addBillModal').modal('hide');
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

    // Reset form khi đóng modal
    $('#addBillModal').on('hidden.bs.modal', function() {
        document.getElementById("bill-form").reset();
        document.getElementById("bill_id").readOnly = false;
        document.querySelector('#addBillModal .modal-title').textContent = 'Thêm phiếu nhập';
        document.querySelector('#addBillModal button[type="submit"]').textContent = 'Lưu';
    });
});

