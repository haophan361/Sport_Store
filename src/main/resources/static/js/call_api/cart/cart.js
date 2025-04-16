function handleAddToCart() {
    const optionId = document.getElementById("optionProductIdInput").value;
    const quantity = document.querySelector(".quantity-input").value;
    window.location.href = `/addtocart/${optionId}/${quantity}`;
}