package com.example.shopingplusassignment.domain.seller.controller;

import com.example.shopingplusassignment.domain.brand.dto.response.BrandResponseDto;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import com.example.shopingplusassignment.domain.seller.dto.request.CreateStoreRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.request.UpdateSellerRequestDto;
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

    /**
     * 셀러 정보 등록 요쳥
     *
     * @param requestDto 셀러 생성 요청 정보가 담긴 {@link CreateStoreRequestDto} 객체
     * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
     * @return 생성된 브랜드 정보가 담긴 {@link SellerResponseDto} 객체
     */
    @PostMapping
    public ResponseEntity<SellerResponseDto> createSeller(
            @Valid @RequestBody CreateStoreRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUser().getId();
        SellerResponseDto sellerResponseDto = sellerService.createSeller(requestDto, userId);
        return new ResponseEntity<>(sellerResponseDto, HttpStatus.CREATED);
    }

    /**
     * 셀러 정보 조회 요청
     *
     * @param sellerId 조회할 셀러의 ID
     * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
     * @return 브랜드 정보가 담긴 {@link SellerResponseDto} 객체
     */
    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerResponseDto> getSeller(
            @PathVariable Long sellerId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUser().getId();
        SellerResponseDto sellerResponseDto = sellerService.getSeller(sellerId, userId);
        return new ResponseEntity<>(sellerResponseDto, HttpStatus.OK);
    }

    /**
     * 셀러 정보 수정 요청
     *
     * @param sellerId 조회할 셀러의 ID
     * @param updateSellerRequestDto 셀러 수정 요청 정보가 담긴 {@link UpdateSellerRequestDto}
     * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
     * @return 수정 브랜드 정보가 담긴 {@link SellerResponseDto} 객체
     */
    @PutMapping("/{sellerId}")
    public ResponseEntity<SellerResponseDto> updateSeller(
            @PathVariable Long sellerId,
            @Valid @RequestBody UpdateSellerRequestDto updateSellerRequestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUser().getId();
        SellerResponseDto sellerResponseDto = sellerService.updateSeller(sellerId, updateSellerRequestDto, userId);
        return new ResponseEntity<>(sellerResponseDto, HttpStatus.OK);
    }

    /**
     * 셀러 정보 수정 요청
     *
     * @param sellerId 조회할 셀러의 ID
     * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
     */
    @DeleteMapping("/{sellerId}")
    public ResponseEntity<Void> deleteSeller(
            @PathVariable Long sellerId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUser().getId();
        sellerService.deleteSeller(sellerId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
