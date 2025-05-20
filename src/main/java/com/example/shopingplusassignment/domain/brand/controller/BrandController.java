package com.example.shopingplusassignment.domain.brand.controller;

import com.example.shopingplusassignment.domain.brand.dto.BrandCreateRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.BrandResponseDto;
import com.example.shopingplusassignment.domain.brand.service.BrandService;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
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
            @Valid @RequestBody BrandCreateRequestDto requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        BrandResponseDto brandResponseDto = brandService.createBrand(requestDto, authUser.getUser());
        return new ResponseEntity<>(brandResponseDto, HttpStatus.CREATED);
    }

}
