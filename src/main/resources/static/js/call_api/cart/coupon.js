document.addEventListener('DOMContentLoaded', function() {
    const applyCouponBtn = document.getElementById('applyCouponBtn');
    const couponInput = document.getElementById('couponInput');
    const couponMessage = document.getElementById('couponMessage');
    const subtotalElement = document.getElementById('subtotal');
    const finalTotalElement = document.getElementById('finalTotal');
    const discountAmountElement = document.getElementById('discountAmount');
    const couponDiscountRow = document.querySelector('.coupon-discount');
    const originalTotalElement = document.getElementById('originalTotalAmount');
    const appliedCouponIdElement = document.getElementById('appliedCouponId');
    const discountPercentageElement = document.getElementById('discountPercentage');
    const checkoutBtn = document.getElementById('checkoutBtn');

    applyCouponBtn.addEventListener('click', function() {
        const couponId = couponInput.value.trim();
        
        if (!couponId) {
            showMessage('Vui lòng nhập mã giảm giá', 'danger');
            return;
        }
        
        // Gọi API để kiểm tra coupon
        fetch(`/api/apply-coupon?couponId=${couponId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.valid) {
                // Coupon hợp lệ
                applyCouponDiscount(data.percentage);
                showMessage(`Áp dụng mã giảm giá ${data.percentage}% thành công!`, 'success');
                appliedCouponIdElement.value = couponId;
                discountPercentageElement.value = data.percentage;
            } else {
                // Coupon không hợp lệ
                showMessage(data.message || 'Mã giảm giá không hợp lệ', 'danger');
                resetCouponDiscount();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showMessage('Có lỗi xảy ra khi áp dụng mã giảm giá', 'danger');
        });
    });

    // Xử lý nút checkout
    checkoutBtn.addEventListener('click', function(event) {
        event.preventDefault();
        
        const couponId = appliedCouponIdElement.value;
        const discountPercentage = discountPercentageElement.value;
        
        // Đường dẫn checkout cơ bản
        let checkoutUrl = '/customer/checkout';
        
        // Nếu có mã giảm giá đã áp dụng, thêm vào URL
        if (couponId) {
            checkoutUrl += `?couponId=${couponId}&discountPercentage=${discountPercentage}`;
        }
        
        // Chuyển hướng đến trang checkout với thông tin coupon
        window.location.href = checkoutUrl;
    });

    function applyCouponDiscount(percentage) {
        const originalTotal = parseFloat(originalTotalElement.value);
        const discountAmount = (originalTotal * percentage) / 100;
        const finalTotal = originalTotal - discountAmount;
        
        // Hiển thị dòng giảm giá
        couponDiscountRow.style.display = 'flex';
        
        // Cập nhật thông tin giảm giá và tổng tiền
        discountAmountElement.textContent = `${discountAmount.toFixed(3)} VNĐ`;
        finalTotalElement.textContent = `${finalTotal.toFixed(3)} VNĐ`;
    }
    
    function resetCouponDiscount() {
        const originalTotal = parseFloat(originalTotalElement.value);
        
        // Ẩn dòng giảm giá
        couponDiscountRow.style.display = 'none';
        
        // Đặt lại tổng tiền ban đầu
        finalTotalElement.textContent = `${originalTotal.toFixed(3)} VNĐ`;
        
        // Xóa thông tin coupon đã áp dụng
        appliedCouponIdElement.value = '';
        discountPercentageElement.value = '0';
    }
    
    function showMessage(message, type) {
        couponMessage.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
        couponMessage.style.display = 'block';
        
        // Tự động ẩn thông báo sau 5 giây
        setTimeout(() => {
            couponMessage.style.display = 'none';
        }, 5000);
    }
}); 