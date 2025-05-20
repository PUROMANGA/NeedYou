package com.example.shopingplusassignment.domain.brand.dto;

import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BrandResponseDto {

    private final Long id;

    private final String brandName;

    private final String sellerName;

    public static BrandResponseDto of(Brand brand) {
        return new BrandResponseDto(
                brand.getId(),
                brand.getName(),
                brand.getSeller().getCeoName()
        );
    }

}
