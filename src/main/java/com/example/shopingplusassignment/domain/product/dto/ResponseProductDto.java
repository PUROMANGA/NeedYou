package com.example.shopingplusassignment.domain.product.dto;

import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.entity.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class ResponseProductDto {

    private Long productId;
    private String brandName;
    private Long sellerId;
    private String name;
    private String description;
    private Long price;
    private Long stock;
    private ProductCategory category;
    private LocalDateTime creatTime;
    private LocalDateTime modifiedTime;

    public ResponseProductDto(Product product) {
        this.productId = product.getId();
        this.brandName = product.getBrand().getName();
        this.sellerId = product.getSellerId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.category = product.getProductCategory();
        this.creatTime = product.getModifiedTime();
        this.modifiedTime = product.getModifiedTime();
    }
}
