package com.example.shopingplusassignment.domain.brand.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateBrandRequestDto {

    @NotBlank(message = "수정할 브랜드 이름을 입력해주세요.")
    private final String brandName;

}
