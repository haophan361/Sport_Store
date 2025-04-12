const bills = [
    {
        bill_supply_id: "PN001",
        supplier_name: "Công ty A",
        supplier_phone: "0909123456",
        supplier_address: "123 Lê Lợi",
        bill_supply_date: "2024-12-01",
        bill_supply_cost: 1500000,
        details: [
            { detailId: 1, product_name: "Bóng đá", option_id: "Size 5", option_quantity: 10, option_cost: 100000 },
            { detailId: 2, product_name: "Áo thể thao", option_id: "M", option_quantity: 5, option_cost: 50000 }
        ]
    },
    {
        bill_supply_id: "PN002",
        supplier_name: "Công ty B",
        supplier_phone: "0912345678",
        supplier_address: "456 Nguyễn Trãi",
        bill_supply_date: "2024-12-10",
        bill_supply_cost: 3200000,
        details: [
            { product_name: "Găng tay", option_id: "L", option_quantity: 20, option_cost: 80000 }
        ]
    }
];

const products = [
    {
        product_id: "SP001",
        product_name: "Bóng chuyền",
        category_id: "CAT01",
        brand_id: "BR01",
        discount_id: "DC01",
        product_detail: "Bóng chuyên dùng cho thi đấu trong nhà.",
        is_active: 1
    },
    {
        product_id: "SP002",
        product_name: "Áo thể thao nam",
        category_id: "CAT02",
        brand_id: "BR02",
        discount_id: null,
        product_detail: "Chất liệu cotton, thấm hút mồ hôi.",
        is_active: 0
    }
];

const productOptionsData = {
    "SP001": [
        {
            option_id: "OP001",
            size: "M",
            quantity: 10,
            cost: 150000,
            discount_id: 1,
            color_id: 1,
            color_name: "Đỏ",
            is_active: true
        },
        {
            option_id: "OP002",
            size: "L",
            quantity: 5,
            cost: 155000,
            discount_id: 1,
            color_id: 2,
            color_name: "Xanh",
            is_active: false
        }
    ],
    "SP002": [
        {
            option_id: "OP003",
            size: "S",
            quantity: 8,
            cost: 120000,
            discount_id: 2,
            color_id: 3,
            color_name: "Trắng",
            is_active: true
        }
    ]
};
