document.addEventListener("DOMContentLoaded", function () {
    var product_id = document.getElementById("product-id").value
    var colors = document.getElementsByName("color");
    Array.from(colors).forEach(function (radio) {
        radio.addEventListener("change", function () {
            var selected_color = document.querySelector('input[name="color"]:checked').value;
            window.location.href = "/web/detail_product/" + product_id + "/" + selected_color;
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    var product_id = document.getElementById("product-id").value
    var colors = document.getElementsByName("color");
    var sizes = document.getElementsByName("size");
    Array.from(sizes).forEach(function (radio) {
        radio.addEventListener("change", function () {
            var selected_color = document.querySelector('input[name="color"]:checked').value;
            var selected_size = document.querySelector('input[name="size"]:checked').value;
            window.location.href = "/web/detail_product/" + product_id + "/" + selected_color + "/" + selected_size;
        });
    });
});