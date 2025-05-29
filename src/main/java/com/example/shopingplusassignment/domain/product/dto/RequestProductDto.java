package com.example.shopingplusassignment.domain.product.dto;

import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter

public class RequestProductDto {

    @NotBlank(message = "이름을 기입해주세요")
    private String name;

    @NotBlank(message = "설명을 기입해주세요")
    private String description;

    @NotNull
    private Long price;

    @NotNull
    private Long stock;

    @NotNull(message = "카테고리를 선택해주세요")
    private ProductCategory productCategory;

    public RequestProductDto(String name, String description, Long price, Long stock, ProductCategory productCategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.productCategory = productCategory;
    }
}
