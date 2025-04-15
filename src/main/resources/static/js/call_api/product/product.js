function fetchProduct() {
    fetch("/getAllProduct")
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
      </tr>
    `;
        tbody.insertAdjacentHTML("beforeend", row);
    });
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

// ==================== THÊM SẢN PHẨM MỚI ====================
function saveProduct() {
    const newProduct = {
        product_id: document.getElementById("product_id").value,
        product_name: document.getElementById("product_name").value,
        description: tinymce.get("product_detail").getContent(),
        category_id: document.getElementById("category_id").value,
        brand_id: document.getElementById("brand_id").value,
        discount_id: document.getElementById("discount_id").value,
        active: document.getElementById("is_active").checked ? 1 : 0
    };
    apiRequest("/admin/insert_product", "POST", {'Content-type': 'application/json'}, JSON.stringify(newProduct),
        null, null, "include", function () {
            document.getElementById("product-form").reset();
            document.getElementById("is_active").checked = true;
            $('#addProductModal').modal('hide');
            fetchProduct();
        })
}

$(document).on("click", "#product-list tr", function () {
    const productId = $(this).data("product-id");
    console.log("Clicked sản phẩm:", productId);
    displayProductOptions(productId);
    $("#sanpham-content").hide();
    $("#product-option-content").show();
    document.getElementById("selected_product_id").value = productId
    fetchColor()
});

