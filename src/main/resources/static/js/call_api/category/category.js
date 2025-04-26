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

let selectedCategoryId = null;

function fetchCategory() {
    fetch("/admin/getAllCategory")
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

                div.onclick = function (e) {
                    if (e.target.tagName === "I") return;

                    const btn = div.closest(".dropdown").querySelector(".dropdown-toggle");
                    btn.innerText = item.category_name;


                    selectedCategoryId = item.category_id;
                    const hiddenInput = document.getElementById("category_id");
                    if (hiddenInput) {
                        hiddenInput.value = item.category_id;
                    }
                };

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

function deleteCategory(categoryId) {
    bootbox.confirm(
        {
            title: "Xác nhận xóa",
            message: "Bạn có chắc muốn loại sản phẩm này?",
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
                    apiRequest("/admin/delete_category?category_id=" + encodeURIComponent(categoryId), "DELETE", {},
                        null, null, null, "include", function () {
                            fetchCategory()
                        })
                }
            }
        })
}


