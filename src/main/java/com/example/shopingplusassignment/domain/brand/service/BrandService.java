package com.example.shopingplusassignment.domain.brand.service;

import com.example.shopingplusassignment.domain.brand.dto.request.CreateBrandRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.request.UpdateBrandRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.response.BrandResponseDto;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import error.CustomRuntimeException;
import error.ExceptionCode;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;

    /**
     * 브랜드 생성 요청 서비스
     *
     * @param requestDto 브랜드 생성 요청 정보가 담긴 {@link CreateBrandRequestDto} 객체
     * @param user 현재 로그인 유저
     * @return 생성된 브랜드 정보가 담긴 {@link BrandResponseDto} 객체
     */
    @Transactional
    @Secured("ROLE_SELLER")
    public BrandResponseDto createBrand(CreateBrandRequestDto requestDto, User user) {

        Seller seller = sellerRepository.findByUser(user) // todo user.getId() -> findby id?
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        Brand brand = Brand.create(requestDto.getBrandName(), seller);
        brandRepository.save(brand);

        return BrandResponseDto.of(brand);
    }

    /**
     * 브랜드 조회 요청 서비스
     *
     * @param brandId 브랜드 정보 식별자
     * @param user 현재 로그인 유저
     * @return 식별자에 해당하는 브랜드 정보가 담긴 {@link BrandResponseDto} 객체
     */
    // 추후 브랜드 조회시 상품 리스트 같이 response 해주기
    @Transactional
    public BrandResponseDto getBrand(Long brandId, User user) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BRAND_CANT_FIND));

        return BrandResponseDto.of(brand);
    }

    /**
     * 브랜드 수정 요청 서비스
     *
     * @param brandId 브랜드 정보 식별자
     * @param requestDto 브랜드 생성 요청 정보가 담긴 {@link UpdateBrandRequestDto} 객체
     * @param user 현재 로그인 유저
     * @return 수정한 브랜드 정보가 담긴 {@link BrandResponseDto} 객체
     */
    @Transactional
    @Secured("ROLE_SELLER")
    public BrandResponseDto updateBrand(Long brandId, UpdateBrandRequestDto requestDto, User user) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BRAND_CANT_FIND));

        brand.update(requestDto.getBrandName());

        return BrandResponseDto.of(brand);
    }

    /**
     * 브랜드 삭제 요청 서비스
     *
     * @param brandId 브랜드 정보 식별자
     * @param user 현재 로그인 유저
     */
    @Transactional
    @Secured("ROLE_SELLER")
    public void deleteBrand(Long brandId, User user) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BRAND_CANT_FIND));

        brandRepository.delete(brand);
    }

}
