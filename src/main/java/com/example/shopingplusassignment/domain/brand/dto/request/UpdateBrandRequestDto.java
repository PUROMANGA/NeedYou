package com.example.shopingplusassignment.domain.brand.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateBrandRequestDto {

    @NotBlank(message = "수정할 브랜드 이름을 입력해주세요.")
    @Size(min = 1, max = 20, message = "브랜드 이름은 1~20자여야 합니다.")
    private final String brandName;

}
