package com.example.shopingplusassignment.domain.brand.dto.response;

import com.example.shopingplusassignment.domain.brand.entity.Brand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BrandResponseDto {

    private final Long id;
    private final String brandName;
    private final String sellerName;
    private final LocalDateTime creatTime;
    private final LocalDateTime modifiedTime;

    public static BrandResponseDto of(Brand brand) {
        return new BrandResponseDto(
                brand.getId(),
                brand.getName(),
                brand.getSeller().getCeoName(),
                brand.getCreatTime(),
                brand.getModifiedTime()
        );
    }

}
