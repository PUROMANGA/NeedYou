package com.example.shopingplusassignment.domain.seller.controller;

import com.example.shopingplusassignment.domain.seller.dto.request.StoreCreateRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.response.SellerResponseDto;
import com.example.shopingplusassignment.domain.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping // 추후 jwt, 예외처리 필
    public ResponseEntity<SellerResponseDto> createSeller(
            @Valid @RequestBody StoreCreateRequestDto requestDto
    ) {
        SellerResponseDto sellerResponseDto = sellerService.createSeller(requestDto);
        return new ResponseEntity<>(sellerResponseDto, HttpStatus.CREATED);
    }


}
