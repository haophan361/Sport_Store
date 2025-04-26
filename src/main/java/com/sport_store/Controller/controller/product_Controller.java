package com.sport_store.Controller.controller;

import com.sport_store.DTO.response.product_Response.product_detail_Response;
import com.sport_store.DTO.response.product_option_Response.product_option_detail_Response;
import com.sport_store.Entity.Colors;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Entity.Products;
import com.sport_store.Service.category_Service;
import com.sport_store.Service.product_Service;
import com.sport_store.Service.product_option_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class product_Controller {
    private final product_Service product_service;
    private final product_option_Service product_option_service;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private final category_Service category_service;

    @GetMapping(value = {"/web/detail_product/{product_id}/{color}/{size}", "/web/detail_product/{product_id}/{color}"})
    public String detail_product(Model model, @PathVariable String product_id,
                                 @PathVariable String color,
                                 @PathVariable(required = false) String size) {
        Products product = product_service.getProductById(product_id);
        List<String> sizes = new ArrayList<>();
        List<Colors> colors = product_option_service.getColorsByProductId(product_id);
        for (Product_Options option : product.getProduct_options()) {
            if (option.getColors().getColor().equals(color) && option.is_active()) {
                sizes.add(option.getOption_size());
            }
        }
        Product_Options current_option;
        if (size != null) {
            current_option = product_option_service.getProduct_OptionBySize_ProductId_Color(product_id, color, size);
        } else {
            current_option = product_option_service.Get_productOption(product_id, color).getFirst();
        }
        String new_price_format = null;
        BigDecimal new_price = product_option_service.Get_newPrice(current_option.getDiscounts(), current_option.getOption_cost());
        if (new_price != null) {
            new_price_format = decimalFormat.format(new_price);
        }

        product_option_detail_Response current_option_detail = product_option_detail_Response
                .builder()
                .option_id(current_option.getOption_id())
                .price(decimalFormat.format(current_option.getOption_cost()))
                .new_price(new_price_format)
                .product_img(current_option.getColors().getProduct_img())
                .color(current_option.getColors())
                .size(current_option.getOption_size())
                .build();
        product_detail_Response response = product_detail_Response
                .builder()
                .product_id(product_id)
                .product_name(product.getProduct_name())
                .description(product.getProduct_detail())
                .current_option(current_option_detail)
                .discount(current_option.getDiscounts())
                .brand(product.getBrands())
                .category(product.getCategories())
                .build();
        model.addAttribute("all_category", category_service.getAllCategories());
        model.addAttribute("selectedColor", color);
        model.addAttribute("colors", colors);
        model.addAttribute("size", product_option_service.order_Size(sizes));
        model.addAttribute("product", response);
        if (size != null) {
            model.addAttribute("selectedSize", size);
        }
        return "web/detail";
    }
}
