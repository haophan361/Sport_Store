function fetchDiscount() {
    fetch("/getAllDiscount")
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        }).then(data => {
        populateSelect("discount_id", data, "Giảm giá", "discount_id", "discount_percentage")
    })
}

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