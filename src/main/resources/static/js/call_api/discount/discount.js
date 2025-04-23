function saveDiscount() {
    const form_Discount = {
        discount_percentage: document.getElementById("new_discount_percentage").value,
        start_date: document.getElementById("new_discount_start").value,
        end_date: document.getElementById("new_discount_end").value,
        active: document.getElementById("new_discount_active").checked ? 1 : 0
    }
    apiRequest("/admin/insert_discount", "POST", {'Content-type': 'application/json'}, JSON.stringify(form_Discount),
        null, null, "include", function () {
            $('#addDiscountModal').modal('hide')
            fetchDiscount()
        })
}

function fetchDiscount() {
    fetch("/getAllDiscount")
        .then((response) => response.json())
        .then((data) => {
            const dropdown = document.getElementById("customDiscountDropdown");
            dropdown.innerHTML = "";
            const div = document.createElement("div");
            div.className = "dropdown-item d-flex justify-content-between align-items-center";
            div.innerHTML = `<span>Không giảm giá</span>`;
            dropdown.appendChild(div)
            data.forEach((item) => {
                const div = document.createElement("div");
                div.className = "dropdown-item d-flex justify-content-between align-items-center";
                div.innerHTML = `
            <span>${item.discount_percentage}%</span>
            <i class="fas fa-trash-alt text-danger" style="cursor:pointer;" onclick="deleteDiscount(${item.discount_id})"></i>
          `;
                dropdown.appendChild(div);
            });

            const addNew = document.createElement("div");
            addNew.className = "dropdown-item text-primary";
            addNew.setAttribute("data-toggle", "modal");
            addNew.setAttribute("data-target", "#addDiscountModal");
            addNew.innerHTML = `<i class="fa fa-plus"></i> Thêm Giảm giá mới`;
            dropdown.appendChild(addNew);
        });
}

function deleteDiscount(discountId) {
    bootbox.confirm(
        {
            title: "Xác nhận xóa",
            message: "Bạn có chắc muốn xóa giảm giá này?",
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
                    apiRequest("/admin/delete_discount?discount_id=" + encodeURIComponent(discountId), "PUT", {},
                        null, null, null, "include", function () {
                            fetchDiscount()
                        })
                }
            }
        })
}
  