package com.example.shopingplusassignment.domain.seller.service;

import com.example.shopingplusassignment.domain.seller.dto.request.StoreCreateRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.response.SellerResponseDto;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerResponseDto createSeller(StoreCreateRequestDto requestDto) {

        // + 접근 권한 예외 추가하기

        Seller seller = Seller.createSeller(
                requestDto.getCompanyName(),
                requestDto.getCeoName(),
                requestDto.getEmail(),
                requestDto.getBusinessNumber(),
                requestDto.getBusinessAddress(),
                requestDto.getCsNumber()
                );

        Seller saved = sellerRepository.save(seller);
        
        return SellerResponseDto.of(saved);
    }

}
