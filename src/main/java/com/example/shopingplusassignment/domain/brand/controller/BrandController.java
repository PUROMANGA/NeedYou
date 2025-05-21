package com.example.shopingplusassignment.domain.brand.controller;

import com.example.shopingplusassignment.domain.brand.dto.CreateBrandRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.BrandResponseDto;
import com.example.shopingplusassignment.domain.brand.service.BrandService;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    private ResponseEntity<BrandResponseDto> createBrand(
            @Valid @RequestBody CreateBrandRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        BrandResponseDto brandResponseDto = brandService.createBrand(requestDto, authUser.getUser());
        return new ResponseEntity<>(brandResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{brandId}")
    private ResponseEntity<BrandResponseDto> getBrand(
            @PathVariable Long brandId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        BrandResponseDto brandResponseDto = brandService.getBrand(brandId, authUser.getUser());
        return new ResponseEntity<>(brandResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{brandId}")
    private ResponseEntity<BrandResponseDto> updateBrand(
            @PathVariable Long brandId,
            @Valid @RequestBody CreateBrandRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        BrandResponseDto brandResponseDto = brandService.updateBrand(brandId, requestDto, authUser.getUser());
        return new ResponseEntity<>(brandResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<Void> deleteBrand(
            @PathVariable Long brandId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        brandService.deleteBrand(brandId, authUser.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
