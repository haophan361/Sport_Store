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

function fetchCategory() {
    fetch("/getAllCategory")
        .then(response => {
            if (response.ok) return response.json();
        })
        .then(data => {
            populateSelect("category_id", data, "Loại", "category_id", "category_name");
        })
}



