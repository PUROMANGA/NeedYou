package com.example.shopingplusassignment.domain.seller.controller;

import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import com.example.shopingplusassignment.domain.seller.dto.request.StoreCreateRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.request.updateSellerRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.response.SellerResponseDto;
import com.example.shopingplusassignment.domain.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    // 판매자 정보 등록
    @PostMapping
    public ResponseEntity<SellerResponseDto> createSeller(
            @Valid @RequestBody StoreCreateRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        SellerResponseDto sellerResponseDto = sellerService.createSeller(requestDto, authUser.getUser());
        return new ResponseEntity<>(sellerResponseDto, HttpStatus.CREATED);
    }

    // 판매자 정보 조회
    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerResponseDto> getSeller(
            @PathVariable Long sellerId,
            @AuthenticationPrincipal AuthUser authUser
    ) { 
        SellerResponseDto sellerResponseDto = sellerService.getSeller(sellerId, authUser.getUser());
        return new ResponseEntity<>(sellerResponseDto, HttpStatus.OK);
    }

    // 판매자 정보 수정
    @PutMapping("/{sellerId}")
    public ResponseEntity<SellerResponseDto> updateSeller(
            @PathVariable Long sellerId,
            @Valid @RequestBody updateSellerRequestDto updateSellerRequestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        SellerResponseDto sellerResponseDto = sellerService.updateSeller(sellerId, updateSellerRequestDto);
        return new ResponseEntity<>(sellerResponseDto, HttpStatus.OK);
    }

    // 판매자 정보 삭제
    @DeleteMapping("/{sellerId}")
    public ResponseEntity<Void> deleteSeller(
            @PathVariable Long sellerId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        sellerService.deleteSeller(sellerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
