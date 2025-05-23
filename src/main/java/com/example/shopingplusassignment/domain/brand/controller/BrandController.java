package com.example.shopingplusassignment.domain.brand.controller;

import com.example.shopingplusassignment.domain.brand.dto.request.CreateBrandRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.request.UpdateBrandRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.response.BrandResponseDto;
import com.example.shopingplusassignment.domain.brand.dto.response.DetailBrandResponseDto;
import com.example.shopingplusassignment.domain.brand.service.BrandService;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    /**
     * 브랜드 생성 요청 컨트롤러 (Seller 한정)
     *
     * @param requestDto 브랜드 생성 요청 정보가 담긴 {@link CreateBrandRequestDto} 객체
     * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
     * @return 생성된 브랜드 정보가 담긴 {@link BrandResponseDto} 객체
     */
    @PostMapping
    private ResponseEntity<BrandResponseDto> createBrand(
            @Valid @RequestBody CreateBrandRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUser().getId();
        BrandResponseDto brandResponseDto = brandService.createBrand(requestDto, userId);
        return new ResponseEntity<>(brandResponseDto, HttpStatus.CREATED);
    }


    /**
     * 전체 브랜드 조회 요청 컨트롤러 (로그인 없이 누구나 접근 가능)
     *
     * @return 브랜드 정보가 담긴 {@link BrandResponseDto} 객체
     */
    @GetMapping
    public ResponseEntity<List<BrandResponseDto>> getAllBrands() {
        List<BrandResponseDto> brandList = brandService.getAllBrands();
        return new ResponseEntity<>(brandList, HttpStatus.CREATED);
    }

    /**
     * 브랜드 개별 조회 요청 컨트롤러 (로그인 없이 누구나 접근 가능)
     *
     * @param brandId 조회할 브랜드의 ID
     * @return 브랜드 정보 및 상품리스트가 담긴 {@link DetailBrandResponseDto} 객체
     */
    @GetMapping("/{brandId}")
    private ResponseEntity<DetailBrandResponseDto> getBrand(
            @PathVariable Long brandId
    ) {
        DetailBrandResponseDto brandResponseDto = brandService.getBrand(brandId);
        return new ResponseEntity<>(brandResponseDto, HttpStatus.OK);
    }

    /**
     * 브랜드 수정 요청 컨트롤러 (Seller 한정)
     *
     * @param brandId　조회할 브랜드의 ID
     * @param requestDto　브랜드 수정 요청 정보가 담긴 {@link UpdateBrandRequestDto} 객체
     * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
     * @return 수정된 브랜드 정보가 담긴 {@link BrandResponseDto} 객체
     */
    @PutMapping("/{brandId}")
    private ResponseEntity<BrandResponseDto> updateBrand(
            @PathVariable Long brandId,
            @Valid @RequestBody UpdateBrandRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUser().getId();
        BrandResponseDto brandResponseDto = brandService.updateBrand(brandId, requestDto, userId);
        return new ResponseEntity<>(brandResponseDto, HttpStatus.OK);
    }

    /**
     * 브랜드 삭제 요청 컨트롤러 (Seller 한정)
     *
     * @param brandId 조회할 브랜드의 ID
     * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
     * @return 메세지 응답, 성공 - 200
     */
    @DeleteMapping("/{brandId}")
    public ResponseEntity<Void> deleteBrand(
            @PathVariable Long brandId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUser().getId();
        brandService.deleteBrand(brandId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
