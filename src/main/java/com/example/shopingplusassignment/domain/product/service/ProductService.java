package com.example.shopingplusassignment.domain.product.service;

import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.product.dto.RequestProductDto;
import com.example.shopingplusassignment.domain.product.dto.ResponseProductDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import error.CustomRuntimeException;
import error.ExceptionCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;

    /**
     * 로그인된 판매자의 email로 seller 객체를 찾아주고, brandId로 brand를 찾아준다음, product 객체에 저장시키고, db에도 저장합니다.
     *
     * @param requestProductDto
     * @param brandId
     * @param email
     * @return
     */

    @Transactional
    public ResponseProductDto postProductService(RequestProductDto requestProductDto, Long brandId, String email) {
        Seller seller = sellerRepository.findByEmail(email);
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.BRAND_CANT_FIND));
        Product product = new Product(requestProductDto, brand, seller.getId());
        return new ResponseProductDto(productRepository.save(product));
    }

    /**
     * 상품을 찾고, 판매자를 찾고, 상품을 등록할 때 등록된 판매자와, 현재 로그인된 판매자를 비교한 뒤, 같다면 수정을 할 수 있게끔 도와줍니다.
     * @param requestProductDto
     * @param brandId
     * @param productId
     * @param email
     * @return
     */

    @Transactional
    public ResponseProductDto patchProductService(RequestProductDto requestProductDto,Long brandId, Long productId, String email) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.PRODUCT_CANT_FIND));
        Seller seller = sellerRepository.findByEmail(email);

        if(!product.getSellerId().equals(seller.getId())) {
            throw new RuntimeException("등록자와 일치하지 않습니다.");
        }

        if(!product.getBrand().getName().equals(requestProductDto.getBrandName())) {
            Brand brand = brandRepository.findByName(requestProductDto.getBrandName());
            product.update(requestProductDto, brand);
        }

        return new ResponseProductDto(productRepository.save(product));
    }

    /**
     * 상품을 찾고, 판매자를 찾고, 상품을 등록할 때 판매자와 현재 로그인된 판매자를 비교한 뒤, 같다면 삭제를 합니다.
     * @param productId
     * @param email
     */
    @Transactional
    public void deleteProductService(Long productId, String email) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.PRODUCT_CANT_FIND));
        Seller seller = sellerRepository.findByEmail(email);
        if(!product.getSellerId().equals(seller.getId())) {
            throw new RuntimeException("등록자와 일치하지 않습니다.");
        }

        productRepository.delete(product);
    }

    /**
     * 상품 단 건 조회
     * @param productId
     * @return
     */
    @Transactional(readOnly = true)
    public ResponseProductDto getProductService(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.PRODUCT_CANT_FIND));
        return new ResponseProductDto(product);
    }

    /**
     * keyword로 db에서 제목 혹은 설명과 같은 상품들을 페이징네이션 해서 보여준다
     * @param keyword
     * @param pageable
     * @return
     */

    @Transactional(readOnly = true)
    public Slice<ResponseProductDto> searchProductService(String keyword, Pageable pageable) {
        Slice<ResponseProductDto> findProductByKeyword = productRepository.findByKeyword(keyword, pageable);
        if(findProductByKeyword.isEmpty()) {
            throw new RuntimeException("검색 결과가 없습니다");
        }
        return findProductByKeyword;
    }
}
