function fetchBrand() {
    fetch("/getAllBrand")
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            populateSelect("brand_id", data, "Thương hiệu", "brand_id", "brand_name")
        })
}

function saveBrand() {
    const brand_name = document.getElementById("new_brand_name").value
    apiRequest("/admin/insert_brand", "POST", {}, brand_name,
        null, null, "include", function () {
            $('#addBrandModal').modal('hide')
            fetchBrand()
        })

}