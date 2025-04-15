package com.sport_store.Controller.api;

import com.sport_store.DTO.request.product_Request.product_request;
import com.sport_store.DTO.response.product_Response.product_admin_Response;
import com.sport_store.Entity.Products;
import com.sport_store.Service.product_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class product_API {
    private final product_Service product_service;

    @PostMapping("/admin/insert_product")
    public ResponseEntity<?> insertProduct(@RequestBody product_request request) {
        product_service.Save_Product(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm sản phẩm thành công"));
    }

    @PostMapping("/admin/delete_product")
    public ResponseEntity<?> deleteProduct(@RequestBody String product_id) {
        product_service.delete_Product(product_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa sản phẩm thành công"));
    }

    @GetMapping("/getAllProduct")
    public List<product_admin_Response> getAllProduct() {
        List<Products> products = product_service.getAllProducts();
        List<product_admin_Response> responses = new ArrayList<>();
        for (Products product : products) {
            product_admin_Response response = product_admin_Response
                    .builder()
                    .product_id(product.getProduct_id())
                    .product_name(product.getProduct_name())
                    .brand(product.getBrands().brand_name)
                    .category(product.getCategories().getCategory_name())
                    .active(product.is_active())
                    .build();
            responses.add(response);
        }
        return responses;
    }
}
