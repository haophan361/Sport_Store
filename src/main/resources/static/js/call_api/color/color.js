function saveColor() {
    const color = document.getElementById("new_color_name").value
    apiRequest("/admin/insert_color", "POST", {}, color, null, null, "include",
        function () {
            $('#addColorModal').modal('hide')
            fetchColor()
        })
}

let selectedColorId = null;

function fetchColor() {
    fetch("/getAllColor")
        .then((response) => response.json())
        .then((data) => {
            const dropdown = document.getElementById("customColorDropdown");
            dropdown.innerHTML = "";

            data.forEach((item) => {
                const div = document.createElement("div");
                div.className = "dropdown-item d-flex justify-content-between align-items-center";
                div.innerHTML = `
          <span>${item.color}</span>
          <i class="fas fa-trash-alt text-danger" style="cursor:pointer;" onclick="deleteColor(${item.color_id})"></i>
        `;

                div.onclick = function (e) {
                    if (e.target.tagName === "I") return;

                    const btn = div.closest(".dropdown").querySelector(".dropdown-toggle");
                    btn.innerText = item.color;

                    selectedColorId = item.color_id;

                    const hiddenInput = document.getElementById("color_id");
                    if (hiddenInput) {
                        hiddenInput.value = item.color_id;
                    }
                };

                dropdown.appendChild(div);
            });

            const addNew = document.createElement("div");
            addNew.className = "dropdown-item text-primary";
            addNew.setAttribute("data-toggle", "modal");
            addNew.setAttribute("data-target", "#addColorModal");
            addNew.innerHTML = `<i class="fa fa-plus"></i> Thêm Màu mới`;
            dropdown.appendChild(addNew);
        });
}

function deleteColor(colorId) {
    bootbox.confirm(
        {
            title: "Xác nhận xóa",
            message: "Bạn có chắc muốn xóa màu này",
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
                    apiRequest("/admin/delete_color?color_id=" + encodeURIComponent(colorId), "DELETE", {},
                        null, null, null, "include", function () {
                            fetchColor()
                        })
                }
            }
        })
}
  
