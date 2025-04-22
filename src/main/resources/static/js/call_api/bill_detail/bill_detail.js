function cancelBill(bill_id) {
    bootbox.confirm(
        {
            title: "Xác nhận xóa thông tin",
            message: "Bạn có muốn hủy đơn hàng này không",
            buttons:
                {
                    confirm:
                        {
                            label: 'Xác nhận',
                        },
                    cancel:
                        {
                            label: 'Hủy',
                        }
                },
            callback: function (result) {
                if (result) {
                    apiRequest("/customer/cancel_order?bill_id=" + encodeURIComponent(bill_id), "PUT", {}, null,
                        null, null, "include", function () {
                            location.reload()
                        })
                }
            }
        })
}