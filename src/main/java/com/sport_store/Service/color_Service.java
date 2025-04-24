package com.sport_store.Service;

import com.sport_store.DTO.request.color_Request.color_Request;
import com.sport_store.Entity.Colors;
import com.sport_store.Entity.Images;
import com.sport_store.Entity.Product_Img;
import com.sport_store.Entity.Products;
import com.sport_store.Repository.color_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class color_Service {
    private final color_Repository color_repository;
    private final image_Service image_service;
    private final product_img_Service product_img_service;
    private final product_Service product_service;

    @Transactional
    public void saveColor(color_Request request, MultipartFile[] files) {
        Colors color = Colors
                .builder()
                .color(request.getColor())
                .build();
        color_repository.save(color);
        Products product = product_service.getProductById(request.getProduct_id());
        List<Images> images = new ArrayList<>();
        for (MultipartFile file : files) {
            String image_url = image_service.upload(file);
            Images image = image_service.saveImage(image_url);
            images.add(image);
        }
        product_img_service.save_Product_Img(product, color, images);
    }

    @Transactional
    public void updateColor(color_Request request, MultipartFile[] files) {
        Colors color = color_repository.findById(request.getColor_id()).orElse(null);
        Products products = product_service.getProductById(request.getProduct_id());
        List<Images> images = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String image_url = image_service.upload(file);
                Images image = image_service.saveImage(image_url);
                images.add(image);
            }
            if (color != null) {
                List<Images> oldImage = new ArrayList<>();
                for (Product_Img product_img : color.getProduct_img()) {
                    oldImage.add(product_img.getImages());
                }
                product_img_service.deleteProductImg(color.getProduct_img());
                image_service.deleteImage(oldImage);
                product_img_service.save_Product_Img(products, color, images);
            }
        }
        if (color != null) {
            color.setColor(request.getColor());
            color_repository.save(color);
        }

    }

    public void deleteColor(int color_id) {
        color_repository.deleteById(color_id);
    }

    public Colors findColorById(int color_id) {
        return color_repository.findById(color_id).orElse(null);
    }

    public List<Colors> getAllColors() {
        return color_repository.findAll();
    }
}
