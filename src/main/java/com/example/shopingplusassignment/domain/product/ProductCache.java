package com.example.shopingplusassignment.domain.product;

import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.dto.ResponseProductDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor

public class ProductCache {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(cacheManager = "cacheManager",value = "product", key = "'cooler'")
    public Slice<ResponseProductDto> getProductCategory(Pageable pageable) {
        Slice<Product> foundCoolerByProductCategory = productRepository.findProductByProductCategory(ProductCategory.COOLER, pageable);
        return foundCoolerByProductCategory.map(ResponseProductDto::new);
    }

    public void savedKeywordByCounting(String keyword, String email) {

        String userValue = "userEmail" + ":" + email + "," + "Keyword" + keyword;

        Boolean isFirst = redisTemplate.opsForValue().setIfAbsent(userValue, "1", Duration.ofHours(4));

        if(Boolean.TRUE.equals(isFirst)) {
            redisTemplate.opsForZSet().incrementScore("SearchKeywords", keyword, 1);
        }
    }
}
