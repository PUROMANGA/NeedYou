package com.example.shopingplusassignment.domain.brand.service;

import com.example.shopingplusassignment.domain.brand.dto.request.CreateBrandRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.response.BrandResponseDto;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import error.CustomRuntimeException;
import error.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;

    public BrandResponseDto createBrand(CreateBrandRequestDto requestDto, User user) {

        if (user.getUserRole() != UserRole.SELLER) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_BRAND_ACCESS);
        }

        Seller seller = sellerRepository.findByUser(user)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        Brand brand = Brand.create(requestDto.getBrandName(), seller);
        brandRepository.save(brand);

        return BrandResponseDto.of(brand);

    }

    // 추후 브랜드 조회시 상품 리스트 같이 response 해주기
    public BrandResponseDto getBrand(Long brandId, User user) {

        if (user.getUserRole() != UserRole.SELLER) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_BRAND_ACCESS);
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BRAND_CANT_FIND));

        return BrandResponseDto.of(brand);

        }

    @Transactional
    public BrandResponseDto updateBrand(Long brandId, CreateBrandRequestDto requestDto, User user) {

        if (user.getUserRole() != UserRole.SELLER) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_BRAND_ACCESS);
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BRAND_CANT_FIND));

        brand.update(requestDto.getBrandName());

        return BrandResponseDto.of(brand);

    }

    @Transactional
    public void deleteBrand(Long brandId, User user) {

        if (user.getUserRole() != UserRole.SELLER) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_BRAND_ACCESS);
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BRAND_CANT_FIND));

        brandRepository.delete(brand);


    }

}
