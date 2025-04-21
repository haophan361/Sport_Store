function placeOrder() {
    const receiverId = document.getElementById('receiverSelect').value;
    const customerId = document.getElementById('customerId').value;

    if (!receiverId) {
        alert('Please select a receiver');
        return;
    }

    const orderData = {
        customer_id: customerId,
        receiver_id: receiverId
    };

    apiRequest("/api/orders/create", "POST", {'Content-type': 'application/json'},
        JSON.stringify(orderData), null, null, "include",
        function (response) {
            if (response && response.bill_id) {
                window.location.href = `/orderconfirmation/${response.bill_id}`;
            } else {
                alert('Error creating order. Please try again.');
            }
        });
}

document.addEventListener('DOMContentLoaded', function() {
    const placeOrderBtn = document.getElementById('placeOrderBtn');
    if (placeOrderBtn) {
        placeOrderBtn.addEventListener('click', placeOrder);
    }
}); 