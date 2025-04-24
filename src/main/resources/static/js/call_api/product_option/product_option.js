function fetchProductOption(product_id) {
    return fetch("/admin/getProductOption?product_id=" + encodeURIComponent(product_id))
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
                let discount_time = ""
                let discount_active = ""
                if (option.discount > 0) {
                    discount_time = `${option.time_start} - ${option.time_end}`;
                    discount_active = option.discount_active === true ? 'Còn' : 'Hết'
                }
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${option.option_id}</td>
                    <td>${option.color}</td>
                    <td>${option.size}</td>
                    <td>${option.quantity}</td>
                    <td>${option.cost.toLocaleString() + ".000 VNĐ"}</td>
                    <td>${option.discount + "%"}</td>
                    <td>${discount_time}</td>
                    <td>${discount_active}</td>
                    <td><img src="${option.image}" width="50" alt="Hình ảnh mẫu sản phẩm"></td>
                    <td><input type="checkbox" ${option.active ? 'checked' : ''} onclick="return false;"></td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-outline-primary me-1" title="Chỉnh sửa" data-toggle="modal"
                         onclick="getProductOptionById('${option.option_id}')" data-target="#addOptionModal">
                            <i class="bi bi-pencil-square"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" title="Xoá" onclick="deleteOption('${option.option_id}')">
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

function deleteOption(option_id) {
    bootbox.confirm(
        {
            title: "Xác nhận xóa thông tin",
            message: "Bạn có muốn hủy đơn hàng này không",
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
                    apiRequest("/admin/delete_product_option?option_id=" + encodeURIComponent(option_id), "PUT", {},
                        null, null, null, "include", function () {
                            displayProductOptions(document.getElementById("selected_product_id").value);
                        })
                }
            }
        })
}

function getProductOptionById(option_id) {
    fetch("/admin/getProductOptionById?option_id=" + encodeURIComponent(option_id))
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            renderUpdateProductOption(data)
        })
}

function renderUpdateProductOption(data) {
    const form = document.getElementById("productOptionForm");

    document.getElementById("color_id").value = data.color_id;
    document.getElementById("discount_id").value = data.discount_id;

    document.getElementById("new_option_size").value = data.size;
    document.getElementById("new_price").value = data.price;

    const modalTitle = document.querySelector("#addOptionModal .modal-title");
    if (modalTitle) {
        modalTitle.textContent = "Cập nhật tùy chọn sản phẩm";
    }

    if (data.color_id) {
        document.getElementById("color_id").value = data.color_id;

        if (data.color) {
            const colorDropdownBtn = document.querySelector('#customColorDropdown').closest('.dropdown').querySelector('.dropdown-toggle');
            if (colorDropdownBtn) {
                colorDropdownBtn.innerText = data.color;
            }
        }
    }

    if (data.discount_id) {
        document.getElementById("discount_id").value = data.discount_id;
        if (typeof selectedDiscountId !== 'undefined') {
            selectedDiscountId = data.discount_id;
        }

        if (data.discount_percentage) {
            const discountDropdownBtn = document.querySelector('#customDiscountDropdown').closest('.dropdown').querySelector('.dropdown-toggle');
            if (discountDropdownBtn) {
                discountDropdownBtn.innerText = `${data.discount_percentage}%`;
            }
        }
    } else {
        document.getElementById("discount_id").value = "";
        if (typeof selectedDiscountId !== 'undefined') {
            selectedDiscountId = null;
        }

        const discountDropdownBtn = document.querySelector('#customDiscountDropdown').closest('.dropdown').querySelector('.dropdown-toggle');
        if (discountDropdownBtn) {
            discountDropdownBtn.innerText = "Không giảm giá";
        }
    }

    if (data.active !== undefined) {
        document.getElementById("newOptionIsActive").checked = data.active === 1;
    }

    const imagePreview = document.getElementById("imagePreview");
    imagePreview.innerHTML = '';
    if (data.img_url) {
        // If image_url is an array
        if (Array.isArray(data.img_url)) {
            data.img_url.forEach(url => {
                const img = document.createElement("img");
                img.src = url;
                img.className = "img-thumbnail m-1";
                img.style.width = "100px";
                img.style.height = "100px";
                imagePreview.appendChild(img);
            });
        } else {
            const img = document.createElement("img");
            img.src = data.img_url;
            img.className = "img-thumbnail m-1";
            img.style.width = "100px";
            img.style.height = "100px";
            imagePreview.appendChild(img);
        }
    }

    const submitButton = form.querySelector("button[type='button']");
    if (submitButton) {
        submitButton.textContent = "Cập nhật";
        submitButton.setAttribute("data-mode", "update");
    }

    // Set a hidden input to indicate we're in update mode
    let updateModeInput = document.getElementById("update_mode");
    if (!updateModeInput) {
        updateModeInput = document.createElement("input");
        updateModeInput.type = "hidden";
        updateModeInput.id = "update_mode";
        updateModeInput.name = "update_mode";
        form.appendChild(updateModeInput);
    }
    updateModeInput.value = "true";

    // Show the modal
    $('#addOptionModal').modal('show');
}