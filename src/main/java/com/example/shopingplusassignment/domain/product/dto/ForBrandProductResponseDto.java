package com.example.shopingplusassignment.domain.product.dto;

import com.example.shopingplusassignment.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ForBrandProductResponseDto {

    private final String name;
    private final String description;
    private final Long price;
    private final Long stock;

    // 브랜드 단건 조회시 사용
    public static ForBrandProductResponseDto of(Product product) {
        return new ForBrandProductResponseDto(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );

    }

}
