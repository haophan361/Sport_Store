package com.sport_store.Repository;

import com.sport_store.Entity.Colors;
import com.sport_store.Entity.Product_Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface product_option_Repository extends JpaRepository<Product_Options, Integer>,
        JpaSpecificationExecutor<Product_Options> {
    @Query("SELECT o FROM Product_Options o WHERE o.products.product_id= :product_id AND o.colors.color= :color")
    List<Product_Options> getOptionProductByColors(String product_id, String color);

    @Query("SELECT o.colors FROM Product_Options o WHERE o.colors.color_id= :color_id AND o.products.product_id= :product_id")
    Colors getColorByProductId_Colors(String product_id, int color_id);

    @Query("SELECT DISTINCT o.colors FROM Product_Options o WHERE o.products.product_id= :product_id")
    List<Colors> getColorByProductId(String product_id);

    @Query("SELECT o FROM Product_Options o WHERE o.products.product_id = :product_id ORDER BY o.colors.color_id")
    List<Product_Options> getProduct_OptionByProductId(String product_id);

    @Query("SELECT o FROM Product_Options o WHERE o.option_size= :size AND o.products.product_id= :product_id AND o.colors.color= :color")
    Product_Options getProduct_OptionBySize_ProductId_Color(String product_id, String color, String size);

    @Query("SELECT DISTINCT(o.option_size) FROM Product_Options o")
    List<String> getAllSize();
}
