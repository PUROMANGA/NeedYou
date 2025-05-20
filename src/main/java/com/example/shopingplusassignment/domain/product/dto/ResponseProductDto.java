package com.example.shopingplusassignment.domain.product.dto;

import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.entity.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class ResponseProductDto {

    private Long productId;
    private Brand brand;
    private Long sellerId;
    private String name;
    private String description;
    private Long price;
    private Long stock;
    private Long rating;
    private ProductCategory category;
    private LocalDateTime creatTime;
    private LocalDateTime modifiedTime;

    public ResponseProductDto(Product product) {
        this.productId = product.getId();
        this.brand = product.getBrand();
        this.sellerId = product.getSellerId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.rating = product.getRating();
        this.category = product.getProductCategory();
        this.creatTime = product.getModifiedTime();
        this.modifiedTime = product.getModifiedTime();
    }
}
