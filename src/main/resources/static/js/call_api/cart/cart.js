function fetchCart() {
    fetch("/customer/getCart")
        .then(response => {
            if (response.ok) {
                return response.json()
            }
        }).then(data => {
        renderCart(data)
    })
}

function renderCart(data) {
    const tbody = document.getElementById('cart-list')
    tbody.innerHTML = "";
    let total_price_cart = 0;
    data.forEach(c => {
        total_price_cart = total_price_cart + c.total_price;
        const row = `
            <tr data-cart-id="${c.cart_id}">
                <td>
                    <img src="${c.image_url}"
                         alt="${c.product_name}"
                         style="width: 50px;">
                </td>
                <td>${c.product_name}</td>
                <td>${c.color}</td>
                <td>${c.size}</td>
                <td>${c.cost + ".000 VNĐ"}</td>
                <td class="align-middle">
                    <div class="input-group quantity mx-auto" style="width: 100px;">
                        <div class="input-group-btn">
                            <button type="button" onclick="updateQuantityCart('${c.cart_id}',${c.quantity},0)"
                               class="btn btn-sm btn-primary btn-minus">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                        <input type="text" class="form-control form-control-sm bg-secondary border-0 text-center"
                               value="${c.quantity}">
                        <div class="input-group-btn">
                            <button type="button" onclick="updateQuantityCart('${c.cart_id}',${c.quantity},1)"
                               class="btn btn-sm btn-primary btn-plus">
                                <i class="fa fa-plus"></i>
                            </button>
                        </div>
                    </div>
                </td>
                <td>${c.total_price + ".000 VNĐ"}</td>
                <td class="align-middle">
                    <button type="button" onclick="deleteCart('${c.cart_id}')" class="btn btn-sm btn-danger">
                        <i class="fa fa-times"></i>
                    </button>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", row);
    })
    document.getElementById("total-cart-price").textContent = total_price_cart + ".000VNĐ"
}

function updateQuantityCart(cart_id, quantity, type) {
    type === 1 ? quantity = quantity + 1 : quantity = quantity - 1
    const update_request = {
        cart_id: cart_id,
        quantity: quantity
    }
    fetch("/customer/updateQuantityCart", {
        method: "PUT",
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(update_request),
        credentials: "include"
    })
        .then(response => {
            if (response.ok) {
                fetchCart()
            }
        })
}

function deleteCart(cart_id) {
    apiRequest("/customer/removeCart?cart_id=" + encodeURIComponent(cart_id), "DELETE", {},
        null, null, null, "include", function () {
            fetchCart()
        })
}

document.addEventListener("DOMContentLoaded", () => {
    fetchCart()
})