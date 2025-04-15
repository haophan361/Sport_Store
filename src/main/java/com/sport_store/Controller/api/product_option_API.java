package com.sport_store.Controller.api;

import com.sport_store.DTO.request.product_option_Request.productOption_request;
import com.sport_store.DTO.response.product_option_Response.product_option_admin_Response;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Service.product_option_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class product_option_API {
    private final product_option_Service product_option_service;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.###");

    @PostMapping("/admin/insert_product_option")
    public ResponseEntity<?> insert_product_options(@RequestPart("product_option_request") productOption_request request, @RequestPart(value = "image_url", required = false) MultipartFile[] files) {
        product_option_service.Save_productOption(request, files);
        return ResponseEntity.ok(Collections.singletonMap("message", "Thêm mẫu sản phẩm thành công"));
    }

    @PostMapping("/admin/delete_product_option")
    public ResponseEntity<?> delete_product_options(@RequestBody int option_id) {
        product_option_service.delete_product_option(option_id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Xóa mẫu sản phẩm thành công"));
    }

    @GetMapping("/getProductOption")
    public List<product_option_admin_Response> getProductOption(@RequestParam String product_id) {
        List<Product_Options> product_options = product_option_service.getProduct_OptionByProductId(product_id);
        List<product_option_admin_Response> responses = new ArrayList<>();
        for (Product_Options option : product_options) {
            int discountPercentage = 0;
            LocalDateTime startDate = null;
            LocalDateTime endDate = null;

            if (option.getDiscounts() != null) {
                discountPercentage = option.getDiscounts().getDiscount_percentage();
                startDate = option.getDiscounts().getDiscount_start_date();
                endDate = option.getDiscounts().getDiscount_end_date();
            }
            product_option_admin_Response response = product_option_admin_Response
                    .builder()
                    .option_id(option.getOption_id())
                    .color(option.getColors().getColor())
                    .size(option.getOption_size())
                    .quantity(option.getOption_quantity())
                    .cost(decimalFormat.format(option.getOption_cost()))
                    .discount(discountPercentage)
                    .start(startDate)
                    .end(endDate)
                    .image(option.getColors().getProduct_img().getFirst().getImages().getImage_url())
                    .active(option.is_active())
                    .build();
            responses.add(response);
        }
        return responses;
    }

}
