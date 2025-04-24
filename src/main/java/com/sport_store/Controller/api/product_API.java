package com.sport_store.Controller.api;

import com.sport_store.DTO.request.product_Request.product_request;
import com.sport_store.DTO.response.product_Response.product_admin_Response;
import com.sport_store.DTO.response.product_Response.product_update_Response;
import com.sport_store.Entity.Products;
import com.sport_store.Service.product_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/admin/update_product")
    public ResponseEntity<?> updateProduct(@RequestBody product_request request) {
        product_service.update_Product(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật sản phẩm thành công"));
    }

    @PutMapping("/admin/delete_product")
    public ResponseEntity<?> deleteProduct(@RequestParam String product_id) {
        product_service.delete_Product(product_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa sản phẩm thành công"));
    }

    @GetMapping("/admin/getAllProduct")
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

    @GetMapping("/admin/getProductById")
    public product_update_Response getProductById(@RequestParam String product_id) {
        Products product = product_service.getProductById(product_id);
        return product_update_Response
                .builder()
                .product_id(product.getProduct_id())
                .product_detail(product.getProduct_detail())
                .product_name(product.getProduct_name())
                .brand_id(product.getBrands().brand_id)
                .brand_name(product.getBrands().brand_name)
                .category_id(product.getCategories().getCategory_id())
                .category_name(product.getCategories().getCategory_name())
                .active(product.is_active())
                .build();
    }
}
