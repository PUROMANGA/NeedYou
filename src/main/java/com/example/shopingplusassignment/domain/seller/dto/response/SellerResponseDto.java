package com.example.shopingplusassignment.domain.seller.dto.response;

import com.example.shopingplusassignment.domain.seller.entity.Seller;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class SellerResponseDto {

    private final Long id;
    private final String companyName;
    private final String ceoName;
    private final String email;
    private final String businessNumber;
    private final String businessAddress;
    private final String csNumber;
    private final LocalDateTime creatTime;
    private final LocalDateTime modifiedTime;

    // sellerResponse용 팩토리 메서드
    public static SellerResponseDto of(Seller seller) {
        return new SellerResponseDto(
                seller.getId(),
                seller.getName(),
                seller.getCeoName(),
                seller.getEmail(),
                seller.getBusinessNumber(),
                seller.getBusinessAddress(),
                seller.getCustomerServiceNumber(),
                seller.getCreatTime(),
                seller.getModifiedTime()
        );
    }

}
