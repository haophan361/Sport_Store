package com.sport_store.Controller.controller;

import com.sport_store.DTO.response.product_Response.product_shop_Response;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Entity.Products;
import com.sport_store.Service.brand_Service;
import com.sport_store.Service.category_Service;
import com.sport_store.Service.product_Service;
import com.sport_store.Service.product_option_Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
    private final brand_Service brand_service;
    private final category_Service category_service;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("all_category", category_service.getAllCategories());
        model.addAttribute("url", "index");
        return "web/index";
    }

    @GetMapping("/web/shop")
    public String shop(Model model, @RequestParam(value = "keyword", defaultValue = "") String keyword,
                       @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "category_id", required = false) Integer category_id,
                       @RequestParam(value = "list_brand", required = false) List<Integer> brands,
                       @RequestParam(value = "list_option_size", required = false) List<String> option_size,
                       @RequestParam(value = "cost_segment", required = false) List<Integer> cost_segment,
                       @RequestParam(value = "page_size", defaultValue = "9") int page_size,
                       @RequestParam(value = "sort", required = false) Boolean sort_asc,
                       HttpServletRequest request) {
        Page<Products> products = product_service.getProduct(keyword, category_id, brands, option_size,
                page, page_size, cost_segment, sort_asc);
        List<product_shop_Response> responses = new ArrayList<>();
        for (Products product : products) {
            if (!product.getProduct_options().isEmpty()) {
                Product_Options default_option = product.getProduct_options().getFirst();
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
        }
        model.addAttribute("all_brand", brand_service.getAllBrand());
        model.addAttribute("all_size", product_option_service.getAllSize());
        model.addAttribute("all_category", category_service.getAllCategories());
        model.addAttribute("products", responses);
        model.addAttribute("current_page", page);
        model.addAttribute("total_pages", products.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selected_brands", brands);
        model.addAttribute("selected_option_size", option_size);
        model.addAttribute("selected_cost_segment", cost_segment);
        model.addAttribute("sort", sort_asc);
        model.addAttribute("page_size", page_size);
        model.addAttribute("url", "shop");
        UriComponents uri = UriComponentsBuilder
                .fromPath(request.getRequestURI())
                .queryParam("keyword", keyword)
                .queryParam("category_id", category_id)
                .queryParam("list_brand", brands)
                .queryParam("list_option_size", option_size)
                .queryParam("cost_segment", cost_segment)
                .queryParam("page_size", page_size)
                .queryParam("sort", sort_asc)
                .build()
                .encode();

        model.addAttribute("requestURI", uri.toUriString());
        return "web/shop";
    }
}
