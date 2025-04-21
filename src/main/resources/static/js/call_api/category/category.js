function saveCategory() {
    const form_Data = new FormData();
    const image_input = document.getElementById("new_category_image")
    const category_image = image_input.files[0];
    form_Data.append("category_name", document.getElementById("new_category_name").value)
    form_Data.append("category_image", category_image)
    apiRequest("/admin/insert_category", "POST", {}, form_Data,
        null, null, "include", function () {
            $('#addCategoryModal').modal('hide')
            fetchCategory()
        })

}

function fetchCategory() {
    fetch("/getAllCategory")
      .then((response) => response.json())
      .then((data) => {
        const dropdown = document.getElementById("customCategoryDropdown");
        dropdown.innerHTML = "";
  
        data.forEach((item) => {
          const div = document.createElement("div");
          div.className = "dropdown-item d-flex justify-content-between align-items-center";
          div.innerHTML = `
            <span>${item.category_name}</span>
            <i class="fas fa-trash-alt text-danger" style="cursor:pointer;" onclick="deleteCategory(${item.category_id})"></i>
          `;
          dropdown.appendChild(div);
        });
  
        const addNew = document.createElement("div");
        addNew.className = "dropdown-item text-primary";
        addNew.setAttribute("data-toggle", "modal");
        addNew.setAttribute("data-target", "#addCategoryModal");
        addNew.innerHTML = `<i class="fa fa-plus"></i> Thêm Loại mới`;
        dropdown.appendChild(addNew);
      });
  }
  
  function fetchBrand() {
    fetch("/getAllBrand")
      .then((response) => response.json())
      .then((data) => {
        const dropdown = document.getElementById("customBrandDropdown");
        dropdown.innerHTML = "";
  
        data.forEach((item) => {
          const div = document.createElement("div");
          div.className = "dropdown-item d-flex justify-content-between align-items-center";
          div.innerHTML = `
            <span>${item.brand_name}</span>
            <i class="fas fa-trash-alt text-danger" style="cursor:pointer;" onclick="deleteBrand(${item.brand_id})"></i>
          `;
          dropdown.appendChild(div);
        });
  
        const addNew = document.createElement("div");
        addNew.className = "dropdown-item text-primary";
        addNew.setAttribute("data-toggle", "modal");
        addNew.setAttribute("data-target", "#addBrandModal");
        addNew.innerHTML = `<i class="fa fa-plus"></i> Thêm Thương hiệu mới`;
        dropdown.appendChild(addNew);
      });
  }
  


