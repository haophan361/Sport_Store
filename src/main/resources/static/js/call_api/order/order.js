function placeOrder() {
    const receiverId = document.getElementById('receiverSelect').value;
    const selectedValue = document.querySelector('input[name="payment"]:checked').value;
    if (!receiverId) {
        bootbox.alert({
            title: "Thông báo lỗi",
            message: "Hãy chọn thông tin nhận hàng",
            backdrop: true,
        });
        return
    }
    if (selectedValue === null) {
        bootbox.alert({
            title: "Thông báo lỗi",
            message: "Vui lòng chọn phương thức thanh toán",
            backdrop: true,
        });
        return
    }
    const formData = {
        receiver_id: receiverId,
        payment_method: parseInt(selectedValue) === 1
    }

    fetch("/customer/order", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData),
        credentials: "include"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Lỗi khi tạo hóa đơn: " + response.status);
            }
            return response.json();
        })
        .then(json => {
            bootbox.alert({
                title: "Thông báo",
                message: json.message,
                backdrop: true,
                callback: function () {
                    window.location.href = "/customer/orderConfirmation?bill_id="
                        + encodeURIComponent(json.data);
                }
            });
        })
        .catch(err => {
            console.error(err);
            bootbox.alert({title: "Lỗi", message: err.message, backdrop: true});
        });
}

document.addEventListener('DOMContentLoaded', function () {
    fetch_infoReceiver()
    fetch_cartCheckout()
    const placeOrderBtn = document.getElementById('placeOrderBtn');
    if (placeOrderBtn) {
        placeOrderBtn.addEventListener('click', placeOrder);
    }
});

function fetch_cartCheckout() {
    fetch("/customer/getCartCheckout")
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        })
        .then(data => {
            renderCartCheckOut(data)
        })
}

function renderCartCheckOut(data, coupon) {
    let container = document.getElementById("cartCheckout")
    if (!container) {
        container = document.createElement('div');
        container.className = 'bg-light p-30 mb-5';
        document.getElementById('cartCheckout').appendChild(container);
    }
    container.innerHTML = '';

    let subtotal = 0;
    let discountAmount = 0;

    const productsSection = document.createElement('div');
    productsSection.className = 'border-bottom';

    const productsHeader = document.createElement('h6');
    productsHeader.className = 'mb-3';
    productsHeader.textContent = 'Sản phẩm';
    productsSection.appendChild(productsHeader);

    data.forEach(cart => {
        const itemTotal = cart.price * cart.quantity;
        subtotal += itemTotal;

        const productRow = document.createElement('div');
        productRow.className = 'd-flex justify-content-between';
        productRow.dataset.productOptionId = cart.cart_id;

        const productInfo = document.createElement('p');
        productInfo.textContent = `${cart.product_name} ${cart.color} ${cart.size} x ${cart.quantity}`;

        const productPrice = document.createElement('p');
        productPrice.textContent = `${itemTotal}.000 VNĐ`;

        productRow.appendChild(productInfo);
        productRow.appendChild(productPrice);
        productsSection.appendChild(productRow);
    });

    container.appendChild(productsSection);

    // Create summary section
    const summarySection = document.createElement('div');

    container.appendChild(summarySection);

    const totalSection = document.createElement('div');
    totalSection.className = 'pt-2';

    const totalRow = document.createElement('div');
    totalRow.className = 'd-flex justify-content-between mt-2';

    const totalLabel = document.createElement('h5');
    totalLabel.textContent = 'Total';

    const finalTotal = subtotal - discountAmount;
    const totalValue = document.createElement('h5');
    totalValue.textContent = `${finalTotal}.000 VNĐ`;

    totalRow.appendChild(totalLabel);
    totalRow.appendChild(totalValue);
    totalSection.appendChild(totalRow);

    container.appendChild(totalSection);
}


function fetch_infoReceiver() {
    fetch("/customer/getInfoReceiver")
        .then(response => {
            if (response.ok) {
                return response.json()

            }
        })
        .then(data => {
            render_infoReceiver(data)
        })
}

function render_infoReceiver(data) {
    const select = document.getElementById("receiverSelect");
    select.innerHTML = "";

    const defaultOption = document.createElement('option');
    defaultOption.value = "";
    defaultOption.textContent = "-- Chọn thông tin nhận hàng --";
    select.appendChild(defaultOption);

    data.forEach(r => {
        const opt = document.createElement('option');
        opt.value = r.infoReceiver_id;
        opt.textContent = `${r.name} – ${r.phone} – ${r.address}`;
        select.appendChild(opt);
    });
}

function form_addInfo_Receiver() {
    document.getElementById('receiver_name').value = '';
    document.getElementById('receiver_phone').value = '';
    document.getElementById('cityName').value = '';
    document.getElementById('districtName').value = '';
    document.getElementById('wardName').value = '';
    document.getElementById('street').value = '';

    var modal = new bootstrap.Modal(document.getElementById('addReceiverModal'));
    modal.show();
}

function add_infoReceiver() {
    const formData = {
        name: document.getElementById('receiver_name').value,
        phone: document.getElementById('receiver_phone').value,
        city: document.getElementById('cityName').value,
        district: document.getElementById('districtName').value,
        ward: document.getElementById('wardName').value,
        street: document.getElementById('street').value
    };
    apiRequest("/customer/add_infoReceiver", "POST", {'Content-type': 'application/json'},
        JSON.stringify(formData), null, null, "include",
        function () {
            $('#addReceiverModal').modal('hide')
            fetch_infoReceiver()
        })
}

$(document).ready(function () {
    $('#addReceiverModal').on('show.bs.modal', function () {
        var cities = document.getElementById("cities");
        var districts = document.getElementById("districts");
        var wards = document.getElementById("wards");

        cities.length = 1;
        districts.length = 1;
        wards.length = 1;

        fetch("/js/call_api/address.json")
            .then(response => {
                return response.json();
            })
            .then(data => {
                renderCity(data);
            })
    });
})
