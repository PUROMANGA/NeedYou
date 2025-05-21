package com.example.shopingplusassignment.domain.seller.service;

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

    @Transactional
    @Secured("ROLE_SELLER")
    public SellerResponseDto createSeller(CreateStoreRequestDto requestDto, Long userId) {

        // 로그인으로 발급된 토큰에서 userId 정보 가져옴 -> user 리포지토리에서 해당 id 찾아 비교 후 있으면 유저 가져와, 없으면
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));

        if (sellerRepository.existsByUserId(userId)) {
            throw new CustomRuntimeException(ExceptionCode.SELLER_ALREADY_EXISTS);
        }

        // 셀러 생성 (request로 받아온 정보 , 유저)
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

    @Transactional(readOnly = true)
    public SellerResponseDto getSeller(Long sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        return SellerResponseDto.of(seller);
    }

    @Transactional
    @Secured("ROLE_SELLER")
    public SellerResponseDto updateSeller(Long sellerId, UpdateSellerRequestDto updateSellerRequestDto, Long userId) {

        Seller seller = sellerRepository.findById(sellerId)
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

    @Transactional
    @Secured("ROLE_SELLER")
    public void deleteSeller(Long sellerId, Long userId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        if (!seller.getUser().getId().equals(userId)) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_SELLER_ACCESS);
        }

        sellerRepository.delete(seller);
    }

}
