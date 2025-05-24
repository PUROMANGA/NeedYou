package com.example.shopingplusassignment.domain.product.dto;

import com.example.shopingplusassignment.domain.product.entity.Product;
import lombok.Getter;

@Getter

public class ProductStockDto {
    private Long amount;
    private Product product;

    public ProductStockDto(Long amount, Product product) {
        this.amount = amount;
        this.product = product;
    }

    public static Long stockSubAmount(Product product, Long amount) {
        return  product.getStock() - amount;
    }
}
