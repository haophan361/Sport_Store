package com.sport_store.Service;

import com.sport_store.DTO.request.product_Request.product_request;
import com.sport_store.Entity.Brands;
import com.sport_store.Entity.Categories;
import com.sport_store.Entity.Product_Options;
import com.sport_store.Entity.Products;
import com.sport_store.Repository.product_Repository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class product_Service {
    private final product_Repository product_repository;
    private final brand_Service brand_service;
    private final category_Service category_service;

    public void Save_Product(product_request request) {
        Brands brand = brand_service.getBrand(request.getBrand_id());
        Categories category = category_service.getCategory(request.getCategory_id());
        Products product = Products
                .builder()
                .product_id(request.getProduct_id())
                .product_name(request.getProduct_name())
                .product_detail(request.getDescription())
                .brands(brand)
                .categories(category)
                .is_active(request.isActive())
                .build();
        product_repository.save(product);
    }

    public void delete_Product(String product_id) {
        Products product = product_repository.findById(product_id).orElse(null);
        if (product != null) {
            product.set_active(false);
            product_repository.save(product);
        }
    }

    public List<Products> getAllProducts() {
        return product_repository.findAll();
    }

    public Products getProductById(String product_id) {
        return product_repository.findById(product_id).orElse(null);
    }

    public Page<Products> getProduct(String name, Integer category_id, List<Integer> brand_id, List<String> size,
                                     int page, int page_size, List<Integer> cost_segment, Boolean sort_asc) {
        Pageable pageable = PageRequest.of(page - 1, page_size);
        Specification<Products> specification = (root, criteriaQuery, criteriaBuilder) -> {
            root.fetch("product_options", JoinType.LEFT);
            Join<Products, Product_Options> product_options =
                    root.join("product_options", JoinType.LEFT);
            product_options.on(criteriaBuilder.conjunction());
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("is_active"), true));
            predicates.add(criteriaBuilder.like(root.get("product_name"), "%" + name + "%"));
            if (category_id != null) {
                predicates.add(criteriaBuilder.equal(root.get("categories").get("category_id"), category_id));
            }
            if (brand_id != null) {
                predicates.add(root.get("brands").get("brand_id").in(brand_id));
            }
            predicates.add(criteriaBuilder.equal(product_options.get("is_active"), true));
            if (size != null && !size.isEmpty()) {
                predicates.add(product_options.get("option_size").in(size));
            }
            if (cost_segment != null && !cost_segment.isEmpty()) {
                List<Predicate> costPredicate = new ArrayList<>();
                for (Integer segment : cost_segment) {
                    switch (segment) {
                        case 1: {
                            costPredicate.add(criteriaBuilder.lt(product_options.get("option_cost"), BigDecimal.valueOf(500)));
                            break;
                        }
                        case 2: {
                            costPredicate.add(criteriaBuilder.between(product_options.get("option_cost"), BigDecimal.valueOf(500),
                                    BigDecimal.valueOf(1000)));
                            break;
                        }
                        case 3: {
                            costPredicate.add(criteriaBuilder.between(product_options.get("option_cost"), BigDecimal.valueOf(1000),
                                    BigDecimal.valueOf(2000)));
                            break;
                        }
                        case 4: {
                            costPredicate.add(criteriaBuilder.between(product_options.get("option_cost"), BigDecimal.valueOf(2000),
                                    BigDecimal.valueOf(3000)));
                            break;
                        }
                        case 5: {
                            costPredicate.add(criteriaBuilder.gt(product_options.get("option_cost"), BigDecimal.valueOf(3000)));
                            break;
                        }
                    }
                }
                predicates.add(criteriaBuilder.or(costPredicate.toArray(new Predicate[0])));
            }
            if ((size != null && !size.isEmpty()) || (cost_segment != null && !cost_segment.isEmpty())) {
                product_options.on(predicates.toArray(new Predicate[0]));
            }
            if (criteriaQuery != null) {
                criteriaQuery.distinct(true);
                if (sort_asc != null) {
                    if (sort_asc) {
                        criteriaQuery.orderBy(criteriaBuilder.asc(product_options.get("option_cost")));
                    } else {
                        criteriaQuery.orderBy(criteriaBuilder.desc(product_options.get("option_cost")));
                    }
                }
            }
            return criteriaBuilder.conjunction();
        };
        return product_repository.findAll(specification, pageable);
    }
}
