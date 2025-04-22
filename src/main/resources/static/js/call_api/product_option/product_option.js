function fetchProductOption(product_id) {
    return fetch("/getProductOption?product_id=" + encodeURIComponent(product_id))
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
}


function displayProductOptions(productId) {
    fetchProductOption(productId)
        .then(options => {
            if (!Array.isArray(options)) {
                options = [];
            }
            if ($.fn.DataTable.isDataTable('#productOptionTable')) {
                $('#productOptionTable').DataTable().clear().destroy();
            }
            const tbody = document.getElementById('product-option-list');
            const container = document.getElementById("product-option-content");
            tbody.innerHTML = '';

            options.forEach(option => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${option.option_id}</td>
                    <td>${option.color || "Không rõ"}</td>
                    <td>${option.size}</td>
                    <td>${option.quantity}</td>
                    <td>${option.cost + ".000 VNĐ"}</td>
                    <td>${option.discount + "%"}</td>
                    <td><img src="${option.image}" width="50" alt="Hình ảnh mẫu sản phẩm"></td>
                    <td><input type="checkbox" ${option.active ? 'checked' : ''}></td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-outline-primary me-1" title="Chỉnh sửa" data-toggle="modal" data-target="#addProductModal">
                        <i class="bi bi-pencil-square"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" title="Xoá">
                        <i class="bi bi-trash"></i>
                        </button>
                    </td>
                `;
                tbody.appendChild(row);
            });

            $(".main-content").hide();
            container.style.display = "block";
            $('#productOptionTable').DataTable({
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
        })
}

function saveProductOption() {
    const productOptionData = {
        color_id: document.getElementById("color_id").value,
        size: document.getElementById("new_option_size").value,
        option_price: document.getElementById("new_price").value,
        product_id: document.getElementById("selected_product_id").value,
        discount_id: document.getElementById("discount_id").value,
        active: document.getElementById("newOptionIsActive").checked ? 1 : 0
    };
    const productOption = new Blob([JSON.stringify(productOptionData)], {type: "application/json"});
    const form_productOption = new FormData()
    form_productOption.append("product_option_request", productOption);
    const option_image_input = document.getElementById("new_option_image");
    if (option_image_input.files.length > 0) {
        for (let i = 0; i < option_image_input.files.length; i++) {
            form_productOption.append("image_url", option_image_input.files[i]);
        }
    }
    apiRequest("/admin/insert_product_option", "POST", {}, form_productOption,
        null, null, "include", function () {
            $('#addOptionModal').modal('hide');
            displayProductOptions(document.getElementById("selected_product_id").value);
        })
}