

function saveBrand() {
    const brand_name = document.getElementById("new_brand_name").value
    apiRequest("/admin/insert_brand", "POST", {}, brand_name,
        null, null, "include", function () {
            $('#addBrandModal').modal('hide')
            fetchBrand()
        })

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

function deleteBrand(brandId) {
    if (confirm("Bạn có chắc muốn xóa thương hiệu này?")) {
        fetch(`/admin/delete_brand/${brandId}`, { method: "DELETE" })
            .then(res => {
                if (res.ok) fetchBrand();
                else alert("Xoá thất bại");
            });
    }
}