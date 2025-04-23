package com.sport_store.Service;

import com.sport_store.DTO.request.product_option_Request.productOption_request;
import com.sport_store.Entity.*;
import com.sport_store.Repository.product_option_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class product_option_Service {
    private final product_option_Repository product_option_repository;
    private final image_Service image_service;
    private final discount_Service discount_service;
    private final product_Service product_service;
    private final product_img_Service product_img_service;
    private final color_Service color_service;

    @Transactional
    public void Save_productOption(productOption_request request, MultipartFile[] files) {
        Products product = product_service.getProductById(request.getProduct_id());
        Colors color = product_option_repository.getColorByProductId_Colors(request.getProduct_id(), request.getColor_id());
        List<Images> images = new ArrayList<>();
        if (color != null && !color.getProduct_img().isEmpty()) {
            for (Product_Img product_img : color.getProduct_img()) {
                images.add(product_img.getImages());
            }
        } else {
            for (MultipartFile file : files) {
                String image_url = image_service.upload(file);
                Images image = image_service.saveImage(image_url);
                images.add(image);
            }
            color = color_service.findColorById(request.getColor_id());
        }
        Product_Options product_option = Product_Options
                .builder()
                .option_size(request.getSize())
                .option_cost(request.getOption_price())
                .option_quantity(0)
                .is_active(request.isActive())
                .products(product)
                .colors(color)
                .discounts(discount_service.getDiscount(request.getDiscount_id()))
                .build();
        product_option_repository.save(product_option);
        product_img_service.save_Product_Img(product, color, images);
    }

    public BigDecimal Get_newPrice(Discounts discount, BigDecimal price) {
        if (discount != null && discount_service.validateDiscount(discount)) {
            BigDecimal discount_price = (new BigDecimal(discount.getDiscount_percentage()).multiply(price))
                    .divide(new BigDecimal(100), 0, RoundingMode.FLOOR);
            return price.subtract(discount_price);
        }
        return price;
    }

    public List<Product_Options> Get_productOption(String product_id, String color) {
        return product_option_repository.getOptionProductByColors(product_id, color);
    }

    public void delete_product_option(int option_id) {
        Product_Options option = product_option_repository.findById(option_id).orElse(null);
        if (option != null) {
            option.set_active(false);
            product_option_repository.save(option);
        }
    }

    public List<String> order_Size(List<String> sizes) {
        if (sizes == null || sizes.isEmpty()) {
            return null;
        }
        String temp = sizes.getFirst();
        if (temp.matches("\\d+")) {
            sizes.sort(Comparator.comparingInt(Integer::parseInt));
        } else {
            List<String> sizeOrder = Arrays.asList("S", "M", "L", "XL");
            sizes.sort(Comparator.comparingInt(sizeOrder::indexOf));
        }
        return sizes;
    }


    public List<Colors> getColorsByProductId(String product_id) {
        return product_option_repository.getColorByProductId(product_id);
    }

    public List<Product_Options> getProduct_OptionByProductId(String product_id) {
        return product_option_repository.getProduct_OptionByProductId(product_id);
    }

    public Product_Options getProduct_OptionBySize_ProductId_Color(String product_id, String color, String size) {
        return product_option_repository.getProduct_OptionBySize_ProductId_Color(product_id, color, size);
    }

    public Product_Options getProduct_Option(int option_id) {
        return product_option_repository.findById(option_id).orElse(null);
    }

    public List<Product_Options> getAllProduct_Option() {
        return product_option_repository.findAll();
    }

    public List<String> getAllSize() {
        return product_option_repository.getAllSize();
    }

}
