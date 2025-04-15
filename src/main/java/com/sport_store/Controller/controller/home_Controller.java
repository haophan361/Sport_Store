package com.sport_store.Controller.controller;

import com.sport_store.DTO.response.product_Response.product_shop_Response;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Entity.Products;
import com.sport_store.Service.product_Service;
import com.sport_store.Service.product_option_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class home_Controller {
    private final product_Service product_service;
    private final product_option_Service product_option_service;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.###");

    @GetMapping("/")
    public String home() {
        return "web/index";
    }

    @GetMapping("/web/shop")
    public String shop(Model model) {
        List<Products> products = product_service.getAllProducts();
        List<product_shop_Response> responses = new ArrayList<>();
        for (Products product : products) {
            Product_Options default_option = product_option_service.getProduct_OptionByProductId(product.getProduct_id()).getFirst();
            BigDecimal price = product.getProduct_options().getFirst().getOption_cost();
            String new_price_format = null;
            BigDecimal new_price = product_option_service.Get_newPrice(default_option.getDiscounts(), price);
            if (new_price != null) {
                new_price_format = decimalFormat.format(new_price);
            }
            product_shop_Response response = product_shop_Response
                    .builder()
                    .product_id(product.getProduct_id())
                    .product_name(product.getProduct_name())
                    .image_url(default_option.getColors().getProduct_img().getFirst().getImages().getImage_url())
                    .price(decimalFormat.format(price))
                    .new_price(new_price_format)
                    .default_url_option(product.getProduct_id() + "/" + default_option.getColors().getColor())
                    .build();
            responses.add(response);
        }
        model.addAttribute("products", responses);
        return "web/shop";
    }
}
