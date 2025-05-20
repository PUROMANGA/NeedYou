package com.example.shopingplusassignment.domain.brand.service;

import com.example.shopingplusassignment.domain.brand.dto.BrandCreateRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.BrandResponseDto;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;

    public BrandResponseDto createBrand(BrandCreateRequestDto requestDto, User user) {

        if (!user.getUserRole().equals(UserRole.SELLER)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        Seller seller = sellerRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 판매자 정보가 없습니다."));

        Brand brand = Brand.create(requestDto.getBrandName(), seller);
        brandRepository.save(brand);

        return BrandResponseDto.of(brand);

    }



}
