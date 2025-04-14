function fetchColor() {
    const product_id = document.getElementById("selected_product_id").value
    fetch("/getAllColor?product_id=" + encodeURIComponent(product_id))
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            populateSelect("color_id", data, "Màu sắc", "color_id", "color")
        })
}

function saveColor() {
    const color = document.getElementById("new_color_name").value
    apiRequest("/admin/insert_color", "POST", {}, color, null, null, "include",
        function () {
            $('#addColorModal').modal('hide')
            fetchColor()
        })
}
