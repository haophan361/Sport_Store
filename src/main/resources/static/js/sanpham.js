function formatCurrency(number) {
    return number.toLocaleString('vi-VN') + ' đ';
}
// ==================== HIỂN THỊ DANH SÁCH SẢN PHẨM ====================
function renderProductList() {
    const tbody = document.getElementById("product-list");
    tbody.innerHTML = "";

    products.forEach(product => {
        const row = `
      <tr data-product-id="${product.product_id}">
        <td>${product.product_id}</td>
        <td>${product.product_name}</td>
        <td>${product.category_id}</td>
        <td>${product.brand_id}</td>
        <td>${product.discount_id ? product.discount_id : "Không có"}</td>
        <td>
          <span class="badge ${product.is_active ? 'badge-success' : 'badge-danger'}">
            ${product.is_active ? 'Đang bán' : 'Ngừng'}
          </span>
        </td>
        <td>${product.product_detail}</td>
      </tr>
    `;
        tbody.insertAdjacentHTML("beforeend", row);
    });
}

// ==================== TẢI SẢN PHẨM ====================
function loadProducts(data = products) {
    const tbody = document.getElementById("product-list");
    tbody.innerHTML = "";
    data.forEach(p => {
        const row = `
      <tr data-product-id="${p.product_id}">
        <td>${p.product_id}</td>
        <td>${p.product_name}</td>
        <td>${p.category_id}</td>
        <td>${p.brand_id}</td>
        <td>${p.discount_id ?? '-'}</td>
        <td>
          <span class="badge ${p.is_active === 1 ? 'badge-success' : 'badge-danger'}">
            ${p.is_active === 1 ? 'Đang bán' : 'Ngừng bán'}
          </span>
        </td>
        <td>${p.product_detail}</td>
      </tr>
    `;
        tbody.insertAdjacentHTML("beforeend", row);
    });
}
function displayProductOptions(productId) {
    const options = productOptionsData[productId] || [];
    const tbody = document.querySelector('#productOptionTable tbody');
    const container = document.getElementById("product-option-content");

    tbody.innerHTML = '';

    options.forEach(option => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${option.option_id}</td>
            <td>${option.color_name || "Không rõ"}</td>
            <td>${option.size}</td>
            <td>${option.quantity}</td>
            <td>${option.cost.toLocaleString()}₫</td>
            <td>${option.discount_id}</td>
            <td><img src="img/placeholder.jpg" width="50" alt="Ảnh mẫu"></td>
            <td><input type="checkbox" ${option.is_active ? 'checked' : ''}></td>
        `;
        tbody.appendChild(row);
    });

    $(".main-content").hide(); // ẩn hết
    container.style.display = "block"; // hiện bảng mẫu
}
// ==================== TÌM KIẾM SẢN PHẨM ====================
function searchProduct() {
    const keyword = document.getElementById("search-product-keyword").value.toLowerCase();
    const filtered = products.filter(p =>
        p.product_id.toLowerCase().includes(keyword) ||
        p.product_name.toLowerCase().includes(keyword)
    );
    loadProducts(filtered);
}

let categories = ["CAT01", "CAT02"];
let brands = ["BR01", "BR02"];
let color = ["Đỏ", "Xanh", "Cam"]
let discounts = ["DC01", "DC02"];

function populateSelect(id, list, label) {
    const select = document.getElementById(id);
    select.innerHTML = list.map(val => `<option value="${val}">${val}</option>`).join('');
    select.innerHTML += `<option value="__add__">➕ Thêm ${label} mới</option>`;
}

function handleAddOption(type) {
    const selectId = `${type}_id`;
    const select = document.getElementById(selectId);
    if (!select) {
        console.error("Không tìm thấy select:", selectId);
        return;
    }

    const value = select.value;

    if (value === "__add__") {
        select.selectedIndex = 0; // Reset lại dropdown

        if (type === 'category') {
            document.getElementById("new_category_name").value = "";
            $('#addCategoryModal').modal('show');
        } else if (type === 'brand') {
            document.getElementById("new_brand_name").value = "";
            $('#addBrandModal').modal('show');
        } else if (type === 'color') {
            document.getElementById("new_color_name").value = "";
            $('#addColorModal').modal('show');
        } else if (type === 'discount') {
            document.getElementById("new_discount_percentage").value = "";
            document.getElementById("new_discount_start").value = "";
            document.getElementById("new_discount_end").value = "";
            document.getElementById("new_discount_active").checked = true;
            $('#addDiscountModal').modal('show');
        }
    }
}


document.getElementById("newOptionImage").addEventListener("change", function () {
    const preview = document.getElementById("imagePreview");
    preview.innerHTML = "";

    Array.from(this.files).forEach(file => {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = document.createElement("img");
            img.src = e.target.result;
            img.className = "img-thumbnail m-1";
            img.style.width = "100px";
            img.style.height = "100px";
            preview.appendChild(img);
        };
        reader.readAsDataURL(file);
    });
});


document.getElementById("search-product-keyword")?.addEventListener("keydown", function(e) {
    if (e.key === "Enter") {
        searchProduct();
    }
});

// ==================== THÊM SẢN PHẨM MỚI ====================
document.getElementById("product-form")?.addEventListener("submit", function (e) {
    e.preventDefault();
    const newProduct = {
        product_id: document.getElementById("product_id").value,
        product_name: document.getElementById("product_name").value,
        product_detail: document.getElementById("product_detail").value,
        category_id: document.getElementById("category_id").value,
        brand_id: document.getElementById("brand_id").value,
        discount_id: document.getElementById("discount_id").value,
        is_active: document.getElementById("is_active").checked ? 1 : 0
    };
    products.push(newProduct);
    loadProducts();
    alert("\u0110\u00e3 th\u00eam s\u1ea3n ph\u1ea9m m\u1edbi!");
    this.reset();
    document.getElementById("is_active").checked = true;
    $('#addProductModal').modal('hide');
});



// ==================== SỰ KIỆN CLICK VÀO DÒNG ====================
document.addEventListener("DOMContentLoaded", () => {
    renderProductList();
    populateSelect("color_id", color, "màu");
    populateSelect("category_id", categories, "loại");
    populateSelect("brand_id", brands, "thương hiệu");
    populateSelect("discount_id", discounts, "giảm giá");

});

$(document).on("click", "#product-list tr", function () {
    const productId = $(this).data("product-id");
    console.log("Clicked sản phẩm:", productId);
    displayProductOptions(productId);
    $("#sanpham-content").hide();
    $("#product-option-content").show();
});
