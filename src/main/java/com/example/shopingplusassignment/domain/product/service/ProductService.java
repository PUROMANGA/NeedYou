package com.example.shopingplusassignment.domain.product.service;

import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.product.ProductCache;
import com.example.shopingplusassignment.domain.product.common.PopularKeywordSetting;
import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.dto.RequestProductDto;
import com.example.shopingplusassignment.domain.product.dto.ResponseProductDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import com.example.shopingplusassignment.error.CustomRuntimeException;
import com.example.shopingplusassignment.error.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;
    private final ProductCache productCache;
    private final PopularKeywordSetting popularKeywordSetting;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

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
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));
        Long userId = user.getId();
        Seller seller = sellerRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("셀러가 없습니다"));
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
        Brand brand = brandRepository.findById(brandId).orElseThrow(() ->new RuntimeException("브랜드 없음"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.PRODUCT_CANT_FIND));
        Seller seller = sellerRepository.findByEmail(email);

        if(!product.getSellerId().equals(seller.getId())) {
            throw new RuntimeException("등록자와 일치하지 않습니다.");
        }

        if(!product.getBrand().getName().equals(brand.getName())) {
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
        productCache.savedKeywordByCounting(keyword);
        Slice<ResponseProductDto> findProductByKeyword = productRepository.findByKeyword(keyword, pageable);
        if(findProductByKeyword.isEmpty()) {
            throw new RuntimeException("검색 결과가 없습니다");
        }
        return findProductByKeyword;
    }


    @Transactional(readOnly = true)
    public Slice<ResponseProductDto> searchProductServicePopularProductCategory(Pageable pageable) {
        long start = System.currentTimeMillis();
        Slice<ResponseProductDto> foundCoolerByProductCategory = productCache.getProductCategory(pageable);
        if(foundCoolerByProductCategory.isEmpty()) {
            throw new RuntimeException("검색 결과가 없습니다");
        }
        long end = System.currentTimeMillis();
        log.info("캐싱 조회 시간: {}ms",(end - start));
        return foundCoolerByProductCategory;
    }

    @Transactional(readOnly = true)
    public Slice<ResponseProductDto> searchProductServicePopularProductCategoryNormal(Pageable pageable) {
        long start = System.currentTimeMillis();
        Slice<ResponseProductDto> foundCoolerByProductCategory = productRepository.findProductByProductCategory(ProductCategory.COOLER, pageable);
        if(foundCoolerByProductCategory.isEmpty()) {
            throw new RuntimeException("검색 결과가 없습니다");
        }
        long end = System.currentTimeMillis();
        log.info("DB 조회 시간: {}ms",(end - start));
        return foundCoolerByProductCategory;
    }

    /**
     * 인기 검색어 api
     */

    @Transactional(readOnly = true)
    public List<String> getPopularKeyword() {
        List<String> getPopularKeyword = popularKeywordSetting.getPopularKeyword();
        return getPopularKeyword;
    }

    /**
     * 인기 검색어로 조회된 값을 줌
     */
    @Transactional(readOnly = true)
    public Slice<ResponseProductDto> getPopularProductsByKeyword(String keyword, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(page, size, sort);
        if(page == 0) {
            Object object = redisTemplate.opsForValue().get("PopularProducts:" + keyword + ":slice:0");

            @SuppressWarnings("unchecked")
            List<ResponseProductDto> responseProductDto = (List<ResponseProductDto>) object;

            boolean hasNext = responseProductDto.size() > size;
            return new SliceImpl<>(responseProductDto.subList(0, Math.min(size, responseProductDto.size())), PageRequest.of(0, size), hasNext);
        }
        return productRepository.findByKeyword(keyword, pageable);
    }
}
