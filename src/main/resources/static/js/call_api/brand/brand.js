

function saveBrand() {
    const brand_name = document.getElementById("new_brand_name").value
    apiRequest("/admin/insert_brand", "POST", {}, brand_name,
        null, null, "include", function () {
            $('#addBrandModal').modal('hide')
            fetchBrand()
        })

}

let selectedBrandId = null; 

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

                // Gán sự kiện click chọn thương hiệu
                div.onclick = function (e) {
                    if (e.target.tagName === "I") return; // Bỏ qua nếu click vào icon

                    const btn = div.closest(".dropdown").querySelector(".dropdown-toggle");
                    btn.innerText = item.brand_name;

                    selectedBrandId = item.brand_id;

                    // Nếu có input ẩn lưu brand_id
                    const hiddenInput = document.getElementById("brandId");
                    if (hiddenInput) hiddenInput.value = item.brand_id;
                };

                dropdown.appendChild(div);
            });

            // Dòng thêm mới
            const addNew = document.createElement("div");
            addNew.className = "dropdown-item text-primary";
            addNew.setAttribute("data-toggle", "modal");
            addNew.setAttribute("data-target", "#addBrandModal");
            addNew.innerHTML = `<i class="fa fa-plus"></i> Thêm Thương hiệu mới`;
            dropdown.appendChild(addNew);
        });
}


function deleteBrand(brandId) {
    if (confirm("Bạn có chắc muốn xóa thương hiệu này?")) {
        fetch(`/admin/delete_brand/${brandId}`, { method: "DELETE" })
            .then(res => {
                if (res.ok) fetchBrand();
                else alert("Xoá thất bại");
            });
    }
}