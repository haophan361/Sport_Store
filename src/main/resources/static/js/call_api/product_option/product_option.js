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
            const tbody = document.querySelector('#productOptionTable tbody');
            const container = document.getElementById("product-option-content");
            tbody.innerHTML = '';

            options.forEach(option => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${option.option_id}</td>
                    <td>${option.color || "Không rõ"}</td>
                    <td>${option.size}</td>
                    <td>${option.cost + ".000₫"}</td>
                    <td>${option.quantity}</td>
                    <td>${option.discount + "%"}</td>
                    <td>${option.discount > 0 ? option.start + " - " + option.end : ""}</td>
                    <td><img src="${option.image}" width="50" alt="Hình ảnh mẫu sản phẩm"></td>
                    <td><input type="checkbox" ${option.active ? 'checked' : ''}></td>
                `;
                tbody.appendChild(row);
            });

            $(".main-content").hide();
            container.style.display = "block";
        })
}

function saveProductOption() {
    const productOptionData = {
        color_id: document.getElementById("color_id").value,
        size: document.getElementById("new_option_size").value,
        option_price: document.getElementById("new_price").value,
        option_quantity: document.getElementById("new_quantity").value,
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