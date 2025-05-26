package com.example.shopingplusassignment.domain;

import com.example.shopingplusassignment.domain.seller.dto.request.CreateStoreRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.request.UpdateSellerRequestDto;
import com.example.shopingplusassignment.domain.seller.dto.response.SellerResponseDto;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.seller.service.SellerService;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import com.example.shopingplusassignment.error.CustomRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.shopingplusassignment.domain.user.entity.User;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SellerService sellerService;

    private User user;
    private Seller seller;

    @BeforeEach
    void setUp() {
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        seller = Seller.createSeller(
                "회사컴퍼니", "김대표", "kim123@example.com",
                "123-1234-12345", "서울특별시", "010-0000-0000", user
        );
        ReflectionTestUtils.setField(seller, "id", 1L);
    }

    @Test
    @DisplayName("셀러 생성 성공")
    void createSeller_success() {
        CreateStoreRequestDto request = new CreateStoreRequestDto(
                "회사컴퍼니", "김대표", "kim123@example.com",
                "123-1234-12345", "서울특별시", "010-0000-0000");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sellerRepository.existsByUserId(1L)).thenReturn(false);
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDto response = sellerService.createSeller(request, 1L);

        assertThat(response.getCompanyName()).isEqualTo("회사컴퍼니");
    }

    @Test
    @DisplayName("셀러 조회 성공")
    void getSeller_success() {
        when(sellerRepository.findByIdFetchUser(1L)).thenReturn(Optional.of(seller));

        SellerResponseDto response = sellerService.getSeller(1L, 1L);

        assertEquals("회사컴퍼니", response.getCompanyName());
    }

    @Test
    @DisplayName("셀러 수정 성공")
    void updateSeller_success() {
        UpdateSellerRequestDto request = new UpdateSellerRequestDto(
                "바뀐 회사명", "바뀐 대표자", "new@example.com", "414-121-12312", "부산광역시", "080-8765-4321");
        when(sellerRepository.findByIdFetchUser(1L)).thenReturn(Optional.of(seller));

        SellerResponseDto response = sellerService.updateSeller(1L, request, 1L);

        assertEquals("바뀐 회사명", response.getCompanyName());
    }

    @Test
    @DisplayName("셀러 삭제 성공")
    void deleteSeller_success() {
        when(sellerRepository.findByIdFetchUser(1L)).thenReturn(Optional.of(seller));

        sellerService.deleteSeller(1L, 1L);

        verify(sellerRepository).delete(seller);
    }

    @Test
    @DisplayName("조회, 수정, 삭제 실패 -> 권한이 SELLER가 아닐 때")
    void unauthorizedAccess_throwsException() {
        User oherUser = new User();
        ReflectionTestUtils.setField(oherUser,"id",2L);

        ReflectionTestUtils.setField(seller, "user", oherUser);

        when(sellerRepository.findByIdFetchUser(1L)).thenReturn(Optional.of(seller));

        assertThrows(CustomRuntimeException.class, () -> sellerService.getSeller(1L, 1L));
        assertThrows(CustomRuntimeException.class, () -> sellerService.updateSeller(1L, mock(UpdateSellerRequestDto.class), 1L));
        assertThrows(CustomRuntimeException.class, () -> sellerService.deleteSeller(1L, 1L));
    }

}
