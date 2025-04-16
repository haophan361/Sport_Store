let discounts = ["DC01", "DC02"];
// Check if products already exists, if not, create it with sample data
if (typeof products === "undefined") {
  var products = [
    {
      product_id: "SP001",
      product_name: "Áo thể thao nam",
      product_detail: "Áo thể thao chất liệu cotton",
      category_id: "CLOTHES",
      brand_id: "NIKE",
      discount_id: "DC01",
      is_active: 1,
    },
    {
      product_id: "SP002",
      product_name: "Giày chạy bộ",
      product_detail: "Giày chạy bộ nhẹ",
      category_id: "SHOES",
      brand_id: "ADIDAS",
      discount_id: "DC02",
      is_active: 1,
    },
  ];
}

// Adding these variables that were missing
let categories = [
  { id: "CLOTHES", name: "Quần áo" },
  { id: "SHOES", name: "Giày dép" },
];

let brands = [
  { id: "NIKE", name: "Nike" },
  { id: "ADIDAS", name: "Adidas" },
];

let color = [
  { id: "RED", name: "Đỏ" },
  { id: "BLUE", name: "Xanh" },
];

// Adding the missing renderProductList function
function renderProductList() {
  const productList = document.getElementById("product-list");
  if (!productList) {
    console.error("Không tìm thấy phần tử product-list");
    return;
  }

  productList.innerHTML = "";
  products.forEach((product) => {
    const row = document.createElement("tr");
    row.setAttribute("data-product-id", product.product_id);
    row.innerHTML = `
            <td>${product.product_id}</td>
            <td>${product.product_name}</td>
            <td>${getCategoryName(product.category_id)}</td>
            <td>${getBrandName(product.brand_id)}</td>
            <td>${product.is_active ? "Hoạt động" : "Không hoạt động"}</td>
            <td>
                <button class="btn btn-sm btn-primary edit-product-btn" data-product-id="${
                  product.product_id
                }">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger delete-product-btn" data-product-id="${
                  product.product_id
                }">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
    productList.appendChild(row);
  });
}

// Helper functions for renderProductList
function getCategoryName(categoryId) {
  const category = categories.find((cat) => cat.id === categoryId);
  return category ? category.name : "Không xác định";
}

function getBrandName(brandId) {
  const brand = brands.find((b) => b.id === brandId);
  return brand ? brand.name : "Không xác định";
}

// Adding the missing displayProductOptions function
function displayProductOptions(productId) {
  console.log("Hiển thị mẫu sản phẩm cho:", productId);

  // Tìm sản phẩm theo ID
  const product = products.find((p) => p.product_id === productId);
  if (!product) {
    console.error("Không tìm thấy sản phẩm với ID:", productId);
    return;
  }

  // Hiển thị thông tin sản phẩm trong header của phần chi tiết
  const productHeader = document.querySelector(
    "#product-option-content .card-header"
  );
  if (productHeader) {
    productHeader.innerHTML = `
            <h6 class="m-0 font-weight-bold text-primary">Chi tiết sản phẩm: ${product.product_name} (${product.product_id})</h6>
            <button class="btn btn-sm btn-secondary ml-2" id="back-to-product-list">
                <i class="fas fa-arrow-left"></i> Quay lại
            </button>
        `;
  }

  // Thiết lập sự kiện nút quay lại
  document
    .getElementById("back-to-product-list")
    ?.addEventListener("click", function () {
      $("#product-option-content").hide();
      $("#sanpham-content").show();
    });

  // TODO: Load danh sách mẫu sản phẩm cho product_id này
  // Đây là nơi bạn sẽ gọi API hoặc tải dữ liệu mẫu sản phẩm
}

// Thêm hàm loadProducts để tránh lỗi
function loadProducts() {
  renderProductList();
}

function populateSelect(id, list, label, valueKey, displayKey) {
  const select = document.getElementById(id);
  select.innerHTML = `<option value="">-- Chọn ${label} --</option>`;
  select.innerHTML += list
    .map(
      (item) => `<option value="${item[valueKey]}">${item[displayKey]}</option>`
    )
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
      document.getElementById("new_category_image").value = "";
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
  .getElementById("new_option_image")
  .addEventListener("change", function () {
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
document
  .getElementById("product-form")
  ?.addEventListener("submit", function (e) {
    e.preventDefault();
    const newProduct = {
      product_id: document.getElementById("product_id").value,
      product_name: document.getElementById("product_name").value,
      product_detail: document.getElementById("product_detail").value,
      category_id: document.getElementById("category_id").value,
      brand_id: document.getElementById("brand_id").value,
      discount_id: document.getElementById("discount_id").value,
      is_active: document.getElementById("is_active").checked ? 1 : 0,
    };
    products.push(newProduct);
    loadProducts();
    alert("\u0110\u00e3 th\u00eam s\u1ea3n ph\u1ea9m m\u1edbi!");
    this.reset();
    document.getElementById("is_active").checked = true;
    $("#addProductModal").modal("hide");
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
