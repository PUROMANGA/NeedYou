package com.example.shopingplusassignment.domain.brand.dto.response;

import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.product.dto.ForBrandProductResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DetailBrandResponseDto {

    private final Long id;
    private final String brandName;
    private final String sellerName;
    private final LocalDateTime creatTime;
    private final LocalDateTime modifiedTime;
    private final List<ForBrandProductResponseDto> productList;

    public static DetailBrandResponseDto of(Brand brand, List<ForBrandProductResponseDto> productList) {
        return new DetailBrandResponseDto(
                brand.getId(),
                brand.getName(),
                brand.getSeller().getCeoName(),
                brand.getCreatTime(),
                brand.getModifiedTime(),
                productList
        );
    }

}
