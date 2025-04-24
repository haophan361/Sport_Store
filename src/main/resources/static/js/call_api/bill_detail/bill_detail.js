function cancelBill(bill_id) {
    bootbox.confirm(
        {
            title: "Xác nhận xóa",
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

function confirmReceive(bill_id) {
    bootbox.confirm(
        {
            title: "Xác nhận nhận hàng",
            message: "Bạn có muốn xác nhận đã nhận hàng",
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
                    apiRequest("/customer/confirm_receive?bill_id=" + encodeURIComponent(bill_id), "PUT", {}, null,
                        null, null, "include", function () {
                            location.reload()
                        })
                }
            }
        })
}