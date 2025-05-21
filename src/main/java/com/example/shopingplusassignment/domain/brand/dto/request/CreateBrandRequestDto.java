package com.example.shopingplusassignment.domain.brand.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateBrandRequestDto {

    @NotBlank(message = "등록할 브랜드 이름을 입력해주세요.")
    private final String brandName;

}
