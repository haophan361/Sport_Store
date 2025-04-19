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

// ==================== SỰ KIỆN CLICK VÀO DÒNG ====================
document.addEventListener("DOMContentLoaded", () => {
  fetchProduct();
  fetchCategory();
  fetchBrand()
  fetchDiscount()
});