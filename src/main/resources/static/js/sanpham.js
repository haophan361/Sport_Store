let discounts = ["DC01", "DC02"];


function populateSelect(id, list, label, valueKey, displayKey) {
    const select = document.getElementById(id);
    select.innerHTML = `<option value="">-- Chọn ${label} --</option>`;
    select.innerHTML += list.map(item => `<option value="${item[valueKey]}">${item[displayKey]}</option>`).join('');
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
            document.getElementById("new_category_image").value = "";
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

function populateDropdownList(items, listId, dropdownId, type) {
    const list = document.getElementById(listId);
    const dropdownButton = document.getElementById(dropdownId);
    const hiddenInput = document.getElementById(`${type}_id`);
    
    // Clear existing items
    list.innerHTML = '';
    
    // Add items
    items.forEach(item => {
        const itemContainer = document.createElement('div');
        itemContainer.className = 'dropdown-item d-flex justify-content-between align-items-center';
        itemContainer.innerHTML = `
            <span>${item.name || item}</span>
            <button class="btn btn-sm btn-danger ml-2" onclick="deleteItem('${type}', '${item.id || item}')">
                <i class="fas fa-trash"></i>
            </button>
        `;
        
        itemContainer.addEventListener('click', (e) => {
            if (!e.target.closest('.btn-danger')) {
                dropdownButton.textContent = item.name || item;
                hiddenInput.value = item.id || item;
            }
        });
        
        list.appendChild(itemContainer);
    });
    
    // Add "Thêm mới" option
    const addNew = document.createElement('div');
    addNew.className = 'dropdown-item text-primary';
    addNew.innerHTML = `<i class="fas fa-plus"></i> Thêm ${type === 'category' ? 'loại' : type === 'brand' ? 'thương hiệu' : 'mã giảm giá'} mới`;
    addNew.addEventListener('click', () => handleAddOption(type));
    list.appendChild(addNew);
}

function deleteItem(type, id) {
    event.stopPropagation();
    const name = event.target.closest('.dropdown-item').querySelector('span').textContent;
    
    if (confirm(`Bạn có chắc muốn xóa ${name}?`)) {
        // Remove from dropdown
        event.target.closest('.dropdown-item').remove();
        
        // Reset selected value if it was selected
        const dropdownButton = document.getElementById(`${type}Dropdown`);
        const hiddenInput = document.getElementById(`${type}_id`);
        if (hiddenInput.value === id) {
            dropdownButton.textContent = `Chọn ${type === 'category' ? 'loại sản phẩm' : type === 'brand' ? 'thương hiệu' : type === 'discount' ? 'mã giảm giá' : 'màu'}`;
            hiddenInput.value = '';
        }
        
        // TODO: Call API to delete item from database
        console.log(`Deleted ${type} with id ${id}`);
    }
}

// Xử lý xóa màu
$(document).on('click', '.color-dropdown .delete-btn', function(e) {
    e.stopPropagation(); // Ngăn dropdown đóng lại
    $(this).closest('.dropdown-item').remove();
    updateColorCount();
});

function updateColorCount() {
    const colorCount = $('.color-dropdown .dropdown-item').length;
    if (colorCount === 0) {
        $('.color-dropdown .dropdown-toggle').text('Chọn màu');
    }
}

document.addEventListener("DOMContentLoaded", () => {
    renderProductList();
    populateSelect("color_id", color, "màu");
    populateSelect("category_id", categories, "loại");
    populateSelect("brand_id", brands, "thương hiệu");
    populateSelect("discount_id", discounts, "giảm giá");

    // Populate dropdowns
    populateDropdownList(categories, 'category_list', 'categoryDropdown', 'category');
    populateDropdownList(brands, 'brand_list', 'brandDropdown', 'brand');
    populateDropdownList(discounts, 'discount_list', 'discountDropdown', 'discount');
    populateDropdownList(color, 'color_list', 'colorDropdown', 'color');
});

document.getElementById("search-product-keyword")?.addEventListener("keydown", function (e) {
    if (e.key === "Enter") {
        searchProduct();
    }
});

document.getElementById("new_option_image").addEventListener("change", function () {
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
