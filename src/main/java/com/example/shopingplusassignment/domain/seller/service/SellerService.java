package com.example.shopingplusassignment.domain.seller.service;

import com.example.shopingplusassignment.domain.seller.dto.request.StoreCreateRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.request.updateSellerRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.response.SellerResponseDto;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import error.CustomRuntimeException;
import error.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    // todo 접근 권한, 로그인 여부 추가

    public SellerResponseDto createSeller(StoreCreateRequestDto requestDto, User user) {

        // 로그인한 사람의 userrole이 seller인지 확인
        if (user.getUserRole() != UserRole.SELLER) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_SELLER_ACCESS);
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

    public SellerResponseDto getSeller(Long sellerId, User user) {

        if (user.getUserRole() != UserRole.SELLER) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_SELLER_ACCESS);
        }

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        return SellerResponseDto.of(seller);
    }

    @Transactional
    public SellerResponseDto updateSeller(Long sellerId, updateSellerRequestDto updateSellerRequestDto, User user) {

        if (user.getUserRole() != UserRole.SELLER) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_SELLER_ACCESS);
        }

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

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
    public void deleteSeller(Long sellerId, User user) {

        if (user.getUserRole() != UserRole.SELLER) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_SELLER_ACCESS);
        }

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.SELLER_NOT_FOUND));

        sellerRepository.delete(seller);
    }

}
