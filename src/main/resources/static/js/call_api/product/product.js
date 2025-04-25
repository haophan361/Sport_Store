function fetchProduct() {
    fetch("/admin/getAllProduct")
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            loadProducts(data)
        })
}


// ==================== TẢI SẢN PHẨM ====================
function loadProducts(data) {
    const tbody = document.getElementById("product-list");
    if ($.fn.DataTable.isDataTable('#productTable')) {
        $('#productTable').DataTable().clear().destroy();
    }
    tbody.innerHTML = "";
    data.forEach(p => {
        const row = `
            <tr data-product-id="${p.product_id}">
                <td>${p.product_id}</td>
                <td>${p.product_name}</td>
                <td>${p.category}</td>
                <td>${p.brand}</td>
                <td>
                    <span class="badge ${p.active === true ? 'badge-success' : 'badge-danger'}">
                        ${p.active === true ? 'Đang bán' : 'Ngừng bán'}
                    </span>
                </td>
                <td class="text-center">
                    <button class="btn btn-sm btn-outline-primary me-1" title="Chỉnh sửa" data-toggle="modal"
                     onclick="getProductById('${p.product_id}')">
                        <i class="bi bi-pencil-square"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" title="Xoá" onclick="deleteProduct('${p.product_id}')">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
                <td class="text-center">
                    <button class="btn btn-info btn-sm" title="Xem" onclick="getProductOption('${p.product_id}')">
                        <i class="fa fa-eye"></i>
                    </button>
                </td>
          </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", row);
    });
    $('#productTable').DataTable({
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


// ==================== THÊM SẢN PHẨM MỚI ====================
function saveProduct() {
    const newProduct = {
        product_id: document.getElementById("product_id").value,
        product_name: document.getElementById("product_name").value,
        description: tinymce.get("product_detail").getContent(),
        category_id: document.getElementById("category_id").value,
        brand_id: document.getElementById("brand_id").value,
        active: document.getElementById("is_active").checked ? 1 : 0
    };
    let url = "/admin/insert_product";
    let method = "POST"
    if (document.getElementById("product-form").getAttribute('data-mode') === "edit") {
        url = "/admin/update_product"
        method = "PUT"
    }
    apiRequest(url, method, {'Content-type': 'application/json'}, JSON.stringify(newProduct),
        null, null, "include", function () {
            document.getElementById("product-form").reset();
            document.getElementById("is_active").checked = true;
            $('#addProductModal').modal('hide');
            fetchProduct();
            const input_product_id = document.getElementById("product_id");
            input_product_id.readOnly = false
        })
}

function deleteProduct(product_id) {
    bootbox.confirm(
        {
            title: "Xác nhận xóa sản phẩm",
            message: "Bạn có muốn xóa sản phẩm này hay không?",
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
                    apiRequest("/admin/delete_product?product_id=" + encodeURIComponent(product_id), "PUT", {},
                        null, null, null, "include", function () {
                            fetchProduct()
                        })
                }
            }
        })
}

function getProductById(product_id) {
    fetch("/admin/getProductById?product_id=" + encodeURIComponent(product_id))
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            renderProductUpdate(data)
        })
}

function renderProductUpdate(data) {
    document.getElementById("product-form").setAttribute("data-mode", "edit");
    const input_product_id = document.getElementById("product_id")
    input_product_id.readOnly = true
    document.getElementById("product_id").value = data.product_id
    document.getElementById("brand_id").value = data.brand_id
    document.getElementById("category_id").value = data.category_id

    document.getElementById("product_name").value = data.product_name
    tinymce.get('product_detail').setContent(data.product_detail);


    const modalTitle = document.querySelector("#addProductModal .modal-title");
    if (modalTitle) {
        modalTitle.textContent = "Cập nhật sản phẩm";
    }
    if (data.brand_name && data.brand_id) {
        const brandDropdown = document.querySelector('#customBrandDropdown').closest('.dropdown').querySelector('.dropdown-toggle');
        if (brandDropdown) {
            brandDropdown.innerText = data.brand_name;
        }
    }
    if (data.category_name && data.category_id) {
        const categoryDropdown = document.querySelector('#customCategoryDropdown').closest('.dropdown').querySelector('.dropdown-toggle')
        if (categoryDropdown) {
            categoryDropdown.innerText = data.category_name
        }
    }
    if (data.active !== undefined) {
        document.getElementById("is_active").checked = data.active === 1;
    }
    $('#addProductModal').modal('show');
}

function getProductOption(product_id) {
    displayProductOptions(product_id)
    document.getElementById("selected_product_id").value = product_id
    $("#sanpham-content").hide();
    $("#product-option-content").show();
    fetchColor()
}
