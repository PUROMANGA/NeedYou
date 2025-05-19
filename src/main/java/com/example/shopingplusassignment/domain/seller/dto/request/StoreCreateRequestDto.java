package com.example.shopingplusassignment.domain.seller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreCreateRequestDto {

    @NotBlank(message = "업체명을 입력해주세요.")
    private final String companyName;

    @NotBlank(message = "대표자명을 입력해주세요.")
    private final String ceoName;

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private final String email;

    @NotBlank(message = "사업자등록번호를 입력해주세요.")
    private final String businessNumber;

    @NotBlank(message = "사업장 소재지를 입력해주세요.")
    private final String businessAddress;

    @NotBlank(message = "고객센터 전화번호를 입력해주세요.")
    private final String csNumber;

}
