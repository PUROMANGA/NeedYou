package com.example.shopingplusassignment.domain;

import com.example.shopingplusassignment.domain.brand.dto.request.CreateBrandRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.request.UpdateBrandRequestDto;
import com.example.shopingplusassignment.domain.brand.dto.response.BrandResponseDto;
import com.example.shopingplusassignment.domain.brand.dto.response.DetailBrandResponseDto;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.brand.service.BrandService;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.error.CustomRuntimeException;
import com.example.shopingplusassignment.error.ExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;
    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private BrandService brandService;

    private Seller seller;
    private Brand brand;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        ReflectionTestUtils.setField(seller, "id", 1L);
        ReflectionTestUtils.setField(seller, "ceoName", "대표자");

        brand = Brand.create("브랜드", seller);
        ReflectionTestUtils.setField(brand, "id", 1L);
    }

    @Test
    @DisplayName("브랜드 생성 성공")
    void createBrand_success() {
        when(sellerRepository.findByUserId(1L)).thenReturn(Optional.of(seller));
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        CreateBrandRequestDto dto = new CreateBrandRequestDto("브랜드");

        BrandResponseDto result = brandService.createBrand(dto, 1L);

        assertThat(result.getBrandName()).isEqualTo("브랜드");
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    @DisplayName("모든 브랜드 조회 성공")
    void getAllBrands_success() {
        when(brandRepository.findAll()).thenReturn(List.of(brand));

        List<BrandResponseDto> result = brandService.getAllBrands();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBrandName()).isEqualTo("브랜드");
    }

    @Test
    @DisplayName("브랜드 조회 성공")
    void getBrand_success() {
        when(brandRepository.findByIdFetchProduct(1L)).thenReturn(Optional.of(brand));

        DetailBrandResponseDto result = brandService.getBrand(1L);

        assertThat(result.getBrandName()).isEqualTo("브랜드");
    }

    @Test
    @DisplayName("브랜드 수정 성공")
    void updateBrand_success() {
        when(brandRepository.findByIdFetchSeller(1L)).thenReturn(Optional.of(brand));

        UpdateBrandRequestDto dto = new UpdateBrandRequestDto("수정된브랜드");
        BrandResponseDto result = brandService.updateBrand(1L, dto, 1L);

        assertThat(result.getBrandName()).isEqualTo("수정된브랜드");
    }

    @Test
    @DisplayName("브랜드 수정 실패 - 권한이 셀러가 아닌 경우")
    void updateBrand_fail_not_Seller() {
        Seller anotherSeller = new Seller();
        ReflectionTestUtils.setField(anotherSeller, "id", 2L);
        Brand anotherBrand = Brand.create("브랜드", anotherSeller);
        ReflectionTestUtils.setField(anotherBrand, "id", 1L);

        when(brandRepository.findByIdFetchSeller(1L)).thenReturn(Optional.of(anotherBrand));

        UpdateBrandRequestDto dto = new UpdateBrandRequestDto("수정");

        assertThatThrownBy(() -> brandService.updateBrand(1L, dto, 1L))
                .isInstanceOf(CustomRuntimeException.class)
                .hasMessageContaining(ExceptionCode.UNAUTHORIZED_BRAND_ACCESS.getMessage());
    }

    @Test
    @DisplayName("브랜드 삭제 성공")
    void deleteBrand_success() {
        when(brandRepository.findByIdFetchSeller(1L)).thenReturn(Optional.of(brand));

        brandService.deleteBrand(1L, 1L);

        verify(brandRepository).delete(brand);
    }

    @Test
    @DisplayName("브랜드 삭제 실패 - 권한이 셀러가 아닌 경우")
    void deleteBrand_fail_not_Seller() {
        Seller anotherSeller = new Seller();
        ReflectionTestUtils.setField(anotherSeller, "id", 2L);
        Brand anotherBrand = Brand.create("브랜드", anotherSeller);
        ReflectionTestUtils.setField(anotherBrand, "id", 1L);

        when(brandRepository.findByIdFetchSeller(1L)).thenReturn(Optional.of(anotherBrand));

        assertThatThrownBy(() -> brandService.deleteBrand(1L, 1L))
                .isInstanceOf(CustomRuntimeException.class)
                .hasMessageContaining(ExceptionCode.UNAUTHORIZED_BRAND_ACCESS.getMessage());
    }

}
