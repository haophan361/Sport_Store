function formatCurrency(number) {
  return number.toLocaleString("vi-VN") + " đ";
}
// ==================== HIỂN THỊ DANH SÁCH SẢN PHẨM ====================
function renderProductList() {
  const tbody = document.getElementById("product-list");
  tbody.innerHTML = "";

  products.forEach((product) => {
    const row = `
      <tr data-product-id="${product.product_id}">
        <td>${product.product_id}</td>
        <td>${product.product_name}</td>
        <td>${product.category_id}</td>
        <td>${product.brand_id}</td>
        <td>
          <span class="badge ${
            product.is_active ? "badge-success" : "badge-danger"
          }">
            ${product.is_active ? "Đang bán" : "Ngừng"}
          </span>
        </td>
        <td class="text-center">
          <button class="btn btn-sm btn-outline-primary me-1" title="Chỉnh sửa" onclick="editProduct('${
            product.product_id
          }')">
            <i class="bi bi-pencil-square"></i>
          </button>
          <button class="btn btn-sm btn-outline-danger" title="Xoá" onclick="deleteProduct('${
            product.product_id
          }')">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      </tr>
    `;
    tbody.insertAdjacentHTML("beforeend", row);
  });
}

// ==================== TẢI SẢN PHẨM ====================
function loadProducts(data = products) {
  const tbody = document.getElementById("product-list");
  tbody.innerHTML = "";
  data.forEach((p) => {
    const row = `
      <tr data-product-id="${p.product_id}">
        <td>${p.product_id}</td>
        <td>${p.product_name}</td>
        <td>${p.category_id}</td>
        <td>${p.brand_id}</td>
        <td>${p.discount_id ?? "-"}</td>
        <td>
          <span class="badge ${
            p.is_active === 1 ? "badge-success" : "badge-danger"
          }">
            ${p.is_active === 1 ? "Đang bán" : "Ngừng bán"}
          </span>
        </td>
        <td class="text-center">
          <button class="btn btn-sm btn-outline-primary me-1" title="Chỉnh sửa" onclick="editProduct('${
            p.product_id
          }')">
            <i class="bi bi-pencil-square"></i>
          </button>
          <button class="btn btn-sm btn-outline-danger" title="Xoá" onclick="deleteProduct('${
            p.product_id
          }')">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      </tr>
    `;
    tbody.insertAdjacentHTML("beforeend", row);
  });
}

function displayProductOptions(productId) {
  const options = productOptionsData[productId] || [];
  const tbody = document.querySelector("#productOptionTable tbody");
  const container = document.getElementById("product-option-content");

  tbody.innerHTML = "";

  options.forEach((option) => {
    const row = document.createElement("tr");
    row.innerHTML = `
            <td>${option.option_id}</td>
            <td>${option.color_name || "Không rõ"}</td>
            <td>${option.size}</td>
            <td>${option.quantity}</td>
            <td>${option.cost.toLocaleString()}₫</td>
            <td>${option.discount_id || "-"}</td>
            <td><img src="img/placeholder.jpg" width="50" alt="Ảnh mẫu"></td>
            <td><span class="badge ${
              option.is_active ? "badge-success" : "badge-danger"
            }">
                ${option.is_active ? "Có" : "Không"}</span></td>
            <td class="text-center">
                <button class="btn btn-sm btn-outline-primary me-1" title="Chỉnh sửa" 
                    onclick="editProductOption(this)" data-option='${JSON.stringify(
                      option
                    )}'>
                    <i class="bi bi-pencil-square"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" title="Xoá" 
                    onclick="deleteProductOption(this)" data-option-id="${
                      option.option_id
                    }">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        `;
    tbody.appendChild(row);
  });

  $(".main-content").hide(); // ẩn hết
  container.style.display = "block"; // hiện bảng mẫu
}
// ==================== TÌM KIẾM SẢN PHẨM ====================
function searchProduct() {
  const keyword = document
    .getElementById("search-product-keyword")
    .value.toLowerCase();
  const filtered = products.filter(
    (p) =>
      p.product_id.toLowerCase().includes(keyword) ||
      p.product_name.toLowerCase().includes(keyword)
  );
  loadProducts(filtered);
}

let categories = ["CAT01", "CAT02"];
let brands = ["BR01", "BR02"];
let color = ["Đỏ", "Xanh", "Cam"];
let discounts = ["DC01", "DC02"];

function populateSelect(id, list, label) {
  const select = document.getElementById(id);
  select.innerHTML = list
    .map((val) => `<option value="${val}">${val}</option>`)
    .join("");
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

    if (type === "category") {
      document.getElementById("new_category_name").value = "";
      $("#addCategoryModal").modal("show");
    } else if (type === "brand") {
      document.getElementById("new_brand_name").value = "";
      $("#addBrandModal").modal("show");
    } else if (type === "color") {
      document.getElementById("new_color_name").value = "";
      $("#addColorModal").modal("show");
    } else if (type === "discount") {
      document.getElementById("new_discount_percentage").value = "";
      document.getElementById("new_discount_start").value = "";
      document.getElementById("new_discount_end").value = "";
      document.getElementById("new_discount_active").checked = true;
      $("#addDiscountModal").modal("show");
    }
  }
}

function populateDropdownList(items, listId, dropdownId, type) {
  const list = document.getElementById(listId);
  const dropdownButton = document.getElementById(dropdownId);
  const hiddenInput = document.getElementById(`${type}_id`);

  // Clear existing items
  list.innerHTML = "";

  // Add items
  items.forEach((item) => {
    const itemContainer = document.createElement("div");
    itemContainer.className =
      "dropdown-item d-flex justify-content-between align-items-center";
    itemContainer.innerHTML = `
            <span>${item.name || item}</span>
            <button class="btn btn-sm btn-danger ml-2" onclick="deleteItem('${type}', '${
      item.id || item
    }')">
                <i class="fas fa-trash"></i>
            </button>
        `;

    itemContainer.addEventListener("click", (e) => {
      if (!e.target.closest(".btn-danger")) {
        dropdownButton.textContent = item.name || item;
        hiddenInput.value = item.id || item;
      }
    });

    list.appendChild(itemContainer);
  });

  // Add "Thêm mới" option
  const addNew = document.createElement("div");
  addNew.className = "dropdown-item text-primary";
  addNew.innerHTML = `<i class="fas fa-plus"></i> Thêm ${
    type === "category"
      ? "loại"
      : type === "brand"
      ? "thương hiệu"
      : "mã giảm giá"
  } mới`;
  addNew.addEventListener("click", () => handleAddOption(type));
  list.appendChild(addNew);
}

function deleteItem(type, id) {
  event.stopPropagation();
  const name = event.target
    .closest(".dropdown-item")
    .querySelector("span").textContent;

  if (confirm(`Bạn có chắc muốn xóa ${name}?`)) {
    // Remove from dropdown
    event.target.closest(".dropdown-item").remove();

    // Reset selected value if it was selected
    const dropdownButton = document.getElementById(`${type}Dropdown`);
    const hiddenInput = document.getElementById(`${type}_id`);
    if (hiddenInput.value === id) {
      dropdownButton.textContent = `Chọn ${
        type === "category"
          ? "loại sản phẩm"
          : type === "brand"
          ? "thương hiệu"
          : type === "discount"
          ? "mã giảm giá"
          : "màu"
      }`;
      hiddenInput.value = "";
    }

    // TODO: Call API to delete item from database
    console.log(`Deleted ${type} with id ${id}`);
  }
}

// Xử lý xóa màu
$(document).on("click", ".color-dropdown .delete-btn", function (e) {
  e.stopPropagation(); // Ngăn dropdown đóng lại
  $(this).closest(".dropdown-item").remove();
  updateColorCount();
});

function updateColorCount() {
  const colorCount = $(".color-dropdown .dropdown-item").length;
  if (colorCount === 0) {
    $(".color-dropdown .dropdown-toggle").text("Chọn màu");
  }
}

document.addEventListener("DOMContentLoaded", () => {
  renderProductList();
  populateSelect("color_id", color, "màu");
  populateSelect("category_id", categories, "loại");
  populateSelect("brand_id", brands, "thương hiệu");
  populateSelect("discount_id", discounts, "giảm giá");

  // Populate dropdowns
  populateDropdownList(
    categories,
    "category_list",
    "categoryDropdown",
    "category"
  );
  populateDropdownList(brands, "brand_list", "brandDropdown", "brand");
  populateDropdownList(
    discounts,
    "discount_list",
    "discountDropdown",
    "discount"
  );
  populateDropdownList(color, "color_list", "colorDropdown", "color");


  
});

document
  .getElementById("search-product-keyword")
  ?.addEventListener("keydown", function (e) {
    if (e.key === "Enter") {
      searchProduct();
    }
  });

document
  .getElementById("newOptionImage")
  ?.addEventListener("change", function () {
    const preview = document.getElementById("imagePreview");
    preview.innerHTML = "";

    Array.from(this.files).forEach((file) => {
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

// ==================== THÊM SẢN PHẨM MỚI ====================
let isEditing = false;

document
  .getElementById("product-form")
  ?.addEventListener("submit", function (e) {
    e.preventDefault();
    const formData = {
      product_id: document.getElementById("product_id").value,
      product_name: document.getElementById("product_name").value,
      product_detail: document.getElementById("product_detail").value,
      category_id: document.getElementById("category_id").value,
      brand_id: document.getElementById("brand_id").value,
      discount_id: document.getElementById("discount_id").value,
      is_active: document.getElementById("is_active").checked ? 1 : 0,
    };

    if (isEditing) {
      // Cập nhật sản phẩm
      const index = products.findIndex(
        (p) => p.product_id === formData.product_id
      );
      if (index !== -1) {
        products[index] = { ...products[index], ...formData };
        alert("Đã cập nhật sản phẩm thành công!");
      }
    } else {
      // Thêm sản phẩm mới
      products.push(formData);
      alert("Đã thêm sản phẩm mới!");
    }

    loadProducts();
    this.reset();
    document.getElementById("is_active").checked = true;
    document.getElementById("product_id").readOnly = false;
    $("#addProductModal").modal("hide");
    isEditing = false;
  });

$("#addProductModal").on("hidden.bs.modal", function () {
  document.getElementById("product-form").reset();
  document.getElementById("product_id").readOnly = false;
  document.querySelector("#addProductModal .modal-title").textContent =
    "Thêm sản phẩm mới";
  document.querySelector('#addProductModal button[type="submit"]').textContent =
    "Lưu sản phẩm";
  isEditing = false;
});

// ==================== SỰ KIỆN CLICK VÀO DÒNG ====================
$(document).on("click", "#product-list tr", function () {
  const productId = $(this).data("product-id");
  console.log("Clicked sản phẩm:", productId);
  displayProductOptions(productId);
  $("#sanpham-content").hide();
  $("#product-option-content").show();
});

// ==================== HANDLER FUNCTIONS ====================
function editProduct(productId) {
  event.stopPropagation();
  isEditing = true;

  // Tìm sản phẩm trong mảng products
  const product = products.find((p) => p.product_id === productId);
  if (product) {
    // Thay đổi tiêu đề modal
    document.querySelector("#addProductModal .modal-title").textContent =
      "Chỉnh sửa sản phẩm";
    document.querySelector(
      '#addProductModal button[type="submit"]'
    ).textContent = "Cập nhật";

    // Điền thông tin vào form
    document.getElementById("product_id").value = product.product_id;
    document.getElementById("product_id").readOnly = true; // Không cho sửa mã sản phẩm
    document.getElementById("product_name").value = product.product_name;
    document.getElementById("product_detail").value =
      product.product_detail || "";

    // Điền các select
    populateSelect("category_id", categories, "loại");
    populateSelect("brand_id", brands, "thương hiệu");
    populateSelect("discount_id", discounts, "giảm giá");

    // Set selected values
    document.getElementById("category_id").value = product.category_id;
    document.getElementById("brand_id").value = product.brand_id;
    document.getElementById("discount_id").value = product.discount_id || "";

    // Set checkbox
    document.getElementById("is_active").checked = product.is_active === 1;

    // Mở modal
    $("#addProductModal").modal("show");
  }
}

function deleteProduct(productId) {
  event.stopPropagation(); // Prevent row click event
  if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
    // TODO: Implement delete functionality
    console.log("Delete product:", productId);
  }
}

function editProductOption(button) {
  const row = button.closest("tr");
  const optionId = row.cells[0].textContent;
  // Lấy dữ liệu từ row và điền vào modal
  $("#newOptionId").val(optionId);
  $("#color_id").val(row.cells[1].textContent);
  $("#newOptionSize").val(row.cells[2].textContent);
  $("#newOptionQuantity").val(row.cells[3].textContent);
  $("#newOptionPrice").val(row.cells[4].textContent);
  $("#newOptionDiscountId").val(row.cells[5].textContent.replace("%", ""));
  $("#newOptionIsActive").prop(
    "checked",
    row.cells[7].querySelector(".badge-success") !== null
  );

  // Mở modal chỉnh sửa
  $("#addOptionModal").modal("show");
}

function deleteProductOption(button) {
  if (confirm("Bạn có chắc chắn muốn xoá mẫu sản phẩm này?")) {
    const row = button.closest("tr");
    const optionId = row.cells[0].textContent;
    // Thực hiện call API xoá mẫu sản phẩm
    // TODO: Implement API call
    row.remove();
  }
}
