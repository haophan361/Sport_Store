function formatCurrency(number) {
    if (number > 0) {
        return number.toLocaleString('vi-VN') + '.000 đ';
    }
}

function fetchProductOption_BillSupply() {
    fetch("/getOption_BillSupply")
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            loadProductOption(data)
        })
}


let list_option = []

function addToBill(data) {
    list_option.push(data)
    displaySelectedOption()
}

function deleteFromBill(option_id) {
    console.log(list_option)
    const index = list_option.findIndex(o => parseInt(o.product_option_id) === option_id);
    if (index !== -1) {
        list_option.splice(index, 1);
        displaySelectedOption()
    }
}

function loadProductOption(data) {
    const tbody = document.getElementById("available-products-list")
    tbody.innerHTML = ''
    data.forEach(o => {
        const row = `
            <tr data-option-id="${o.product_option_id}">
                <td>${o.product_option_id}</td>
                <td>${o.product_name}</td>
                <td>${o.color}</td>
                <td>${o.size}</td>
                <td>
                    <button onclick="addToBill({product_option_id: '${o.product_option_id}',
                     product_name: '${o.product_name}',
                     color:'${o.color}',
                     size:'${o.size}'})" 
                    type="button" class="btn btn-sm btn-success add-product-btn" data-toggle="tooltip" title="Thêm vào phiếu nhập">
                <i class="fas fa-plus"></i>
            </button>
                </td>
            </tr>
            `;
        tbody.insertAdjacentHTML("beforeend", row);
    })
    if ($.fn.DataTable.isDataTable('#productOptionTable')) {
        $('#productOptionTable').DataTable().clear().destroy()
    }
    $('#productOptionTable').DataTable({
        pageLength: 6,
        language: {
            search: "Tìm kiếm mẫu sản phẩm",
            lengthMenu: "Hiển thị _MENU_ bản ghi mỗi trang",
            info: "Trang _PAGE_ trong tổng số _PAGES_ trang",
            paginate: {
                previous: "Trước",
                next: "Sau"
            }
        }
    })
}

let table_selected_option = $('#selectedOptionTable').DataTable({
    pageLength: 6,
    language: {
        search: "Tìm kiếm mẫu sản phẩm",
        lengthMenu: "Hiển thị _MENU_ bản ghi mỗi trang",
        info: "Trang _PAGE_ trong tổng số _PAGES_ trang",
        paginate: {
            previous: "Trước",
            next: "Sau"
        }
    }
})

function displaySelectedOption() {
    table_selected_option.clear()
    const tbody = document.getElementById("selected-products-list")
    tbody.innerHTML = ''
    list_option.forEach(o => {
        const row = `
            <tr data-option-id="${o.option_id}">
                <td>${o.product_option_id}</td>
                <td>${o.product_name}</td>
                <td>${o.color}</td>
                <td>${o.size}</td>
                <td>
                    <input type="number" class="form-control form-control-sm product-quantity" min="1" style="width: 80px"/>
                </td>
                <td>
                    <div class="input-group input-group-sm"> 
                        <input type="text" class="form-control form-control-sm product-price" min="0" style="width: 100px"/>
                    </div>
                </td>
                <td>
                    <button onclick="deleteFromBill(${o.product_option_id})" type="button" class="btn btn-sm btn-danger remove-product-btn" data-toggle="tooltip" title="Xóa sản phẩm">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
            `;
        tbody.insertAdjacentHTML("beforeend", row);
    })
    document.getElementById("total-product-count").innerText = list_option.length;
}

function fetchBillSupply() {
    fetch("/getAllBillSupply")
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        }).then(data => {
        if (data !== null) {
            loadBills(data)
        }
    })
}

// ==================== TẢI DANH SÁCH PHIẾU NHẬP ====================
function loadBills(data) {
    const tbody = document.getElementById("bill-supply-list");
    if ($.fn.DataTable.isDataTable('#supplierBillTable')) {
        $('#supplierBillTable').DataTable().clear().destroy();
    }
    tbody.innerHTML = "";
    let total = 0;
    data.forEach(b => {
        total += b.bill_supply_cost;
        tbody.innerHTML += `
        <tr data-bill-supply-id="${b.bill_supply_id}">
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

    $('#supplierBillTable').DataTable({
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

function saveBillSupply() {
    const newBillSupply = {
        supplier_name: document.getElementById("supplier_name").value,
        supplier_phone: document.getElementById("supplier_phone").value,
        supplier_address: document.getElementById("supplier_address").value,
        bill_supply_date: document.getElementById("bill_date").value,
        detail_request: []
    }
    const rows = document.querySelectorAll("#selected-products-list tr");
    rows.forEach(row => {
        const cells = row.querySelectorAll("td")
        const option_id = parseInt(cells[0].textContent.trim(), 10);
        const product_name = cells[1].textContent.trim();
        const quantity = parseInt(row.querySelector(".product-quantity").value, 10);
        const cost = parseInt(row.querySelector(".product-price").value, 10);
        const detail = {
            option_id,
            product_name,
            quantity,
            cost
        };
        newBillSupply.detail_request.push(detail);
    })
    apiRequest("/admin/insert_supplier_bill", "POST", {'Content-type': 'application/json'},
        JSON.stringify(newBillSupply), null, null, "include",
        function () {
            $('#addBillModal').modal('hide');
            document.getElementById("bill-form").reset();
            list_option = []
            fetchBillSupply()
        })
}


// ==================== DOMContentLoaded ====================
document.addEventListener("DOMContentLoaded", () => {
    fetchBillSupply();
    fetchProductOption_BillSupply()
});