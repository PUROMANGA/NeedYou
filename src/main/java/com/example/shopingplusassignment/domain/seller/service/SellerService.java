package com.example.shopingplusassignment.domain.seller.service;

import com.example.shopingplusassignment.domain.brand.dto.response.BrandResponseDto;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.seller.dto.request.CreateStoreRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.request.UpdateSellerRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.response.SellerResponseDto;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import error.CustomRuntimeException;
import error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    /**
     * 셀러 생성 요청 서비스
     *
     * @param requestDto 셀러 생성 요청 정보가 담긴 {@link CreateStoreRequestDto}
     * @param userId 현재 로그인 유저 식별자
     * @return 생성된 셀러 정보가 담긴 {@link SellerResponseDto} 객체
     */
    @Transactional
    @Secured("ROLE_SELLER")
    public SellerResponseDto createSeller(CreateStoreRequestDto requestDto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));

        if (sellerRepository.existsByUserId(userId)) {
            throw new CustomRuntimeException(ExceptionCode.SELLER_ALREADY_EXISTS);
        }

        Seller seller = Seller.createSeller(
                requestDto.getCompanyName(),
                requestDto.getCeoName(),
                requestDto.getEmail(),
                requestDto.getBusinessNumber(),
                requestDto.getBusinessAddress(),
                requestDto.getCsNumber(),
                user
                );

        Seller saved = sellerRepository.save(seller);
        
        return SellerResponseDto.of(saved);
    }

    /**
     * 셀러 조회 요청 서비스
     *
     * @param sellerId 셀러 정보 식별자
     * @param userId 현재 로그인 유저 식별자
     * @return 식별자에 해당하는 셀러 정보가 담긴 {@link SellerResponseDto} 객체
     */
    @Transactional(readOnly = true)
    @Secured("ROLE_SELLER")
    public SellerResponseDto getSeller(Long sellerId, Long userId) {

        Seller seller = sellerRepository.findByIdFetchUser(sellerId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        if (!seller.getUser().getId().equals(userId)) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_SELLER_ACCESS);
        }

        return SellerResponseDto.of(seller);
    }

    /**
     * 셀러 수정 요청 서비스
     *
     * @param sellerId 셀러 정보 식별자
     * @param updateSellerRequestDto 셀러 수정 요청 정보가 담긴 {@link UpdateSellerRequestDto}
     * @param userId 현재 로그인 유저 식별자
     * @return 수정된 셀러 정보가 담긴 {@link SellerResponseDto} 객체
     */
    @Transactional
    @Secured("ROLE_SELLER")
    public SellerResponseDto updateSeller(Long sellerId, UpdateSellerRequestDto updateSellerRequestDto, Long userId) {

        Seller seller = sellerRepository.findByIdFetchUser(sellerId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        // 본인의 seller 정보만 수정 가능
        if (!seller.getUser().getId().equals(userId)) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_SELLER_ACCESS);
        }

        seller.update(
                updateSellerRequestDto.getCompanyName(),
                updateSellerRequestDto.getCeoName(),
                updateSellerRequestDto.getEmail(),
                updateSellerRequestDto.getBusinessNumber(),
                updateSellerRequestDto.getBusinessAddress(),
                updateSellerRequestDto.getCsNumber()
                );

        return SellerResponseDto.of(seller);
    }

    /**
     *  셀러 삭제 요청 서비스
     *
     * @param sellerId 셀러 정보 식별자
     * @param userId 현재 로그인 유저 식별자
     */
    @Transactional
    @Secured("ROLE_SELLER")
    public void deleteSeller(Long sellerId, Long userId) {

        Seller seller = sellerRepository.findByIdFetchUser(sellerId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        if (!seller.getUser().getId().equals(userId)) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_SELLER_ACCESS);
        }

        sellerRepository.delete(seller);
    }

}
