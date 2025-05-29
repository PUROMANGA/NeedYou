package com.example.shopingplusassignment.domain;


import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.product.ProductCache;
import com.example.shopingplusassignment.domain.product.common.PopularKeywordSetting;
import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class ProductServiceTest {

    final Logger log = LoggerFactory.getLogger(ProductServiceTest.class);

    @Autowired
    private PopularKeywordSetting popularKeywordSetting;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductCache productCache;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void madePopularCache() {

        String email = "test@email.com";
        String[] emails = new String[350];

        for(int i = 0; i < 200; i ++) {
            emails[i] = "test" + i + email;
            String keywordNumber1 = "엄청난 반팔";
            productCache.savedKeywordByCounting(keywordNumber1, emails[i]);
        }

        for(int i = 200; i < 300; i ++) {
            emails[i] = "test" + i + "email";
            String keywordNumber2 = "엄청난 게임기";
            productCache.savedKeywordByCounting(keywordNumber2, emails[i]);

        }

        for(int i = 300; i < 350; i ++) {
            emails[i] = "test" + i + "email";
            String keywordNumber3 = "대단한 컴퓨터";
            productCache.savedKeywordByCounting(keywordNumber3, emails[i]);
        }

    }

    @Test
    @DisplayName("인기 키워드 캐싱")
    public void testAddPopularKeyword() {

        //given, //when
        Set<ZSetOperations.TypedTuple<Object>> topKeywords = redisTemplate.opsForZSet().reverseRangeWithScores("SearchKeywords", 0, 9);

        //then
        for (ZSetOperations.TypedTuple<Object> tuple : topKeywords) {
            log.info("Keyword: {}, Score: {}", tuple.getValue(), tuple.getScore());
        }

        assertThat(topKeywords).isNotEmpty();
    }

    @Test
    @DisplayName("인기 상품 캐싱")
    public void testAddPopularProductByKeyword() throws JsonProcessingException {

        User user = new User(
                "김땡중",
                "testpw123",
                "test@email.com",
                "0101234564",
                UserRole.USER,
                false
        );

        userRepository.save(user);

        //given
        List<Object> savedPopularProducts = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        Seller seller = new Seller(
               "스파르타",
               "김호중",
               "seller@email.com",
               "1234",
               "우리집",
                "5678",
                user
        );

        sellerRepository.save(seller);

        Brand brand = new Brand(
                "삼성",
                seller
        );

        brandRepository.save(brand);

        List<Product> products = new ArrayList<>();

        for(int i = 1; i <= 500; i ++) {
            Product product = new Product(
                    "엄청난 상품" + i,
                    "설명",
                    5000000L,
                    5L,
                    ProductCategory.COOLER,
                    brand,
                    seller.getId()
            );
            products.add(product);
        }

        productRepository.saveAll(products);

        Pageable pageable = PageRequest.of(0, 10);
        Set<ZSetOperations.TypedTuple<Object>> topKeywords = redisTemplate.opsForZSet().reverseRangeWithScores("SearchKeywords", 0, 9);
        List<String> result = topKeywords.stream().map(tuple -> tuple.getValue().toString()).toList();


        //when
        for (String keyword : result) {
            //엄청난, 엘라스틱 서치, 형태소분리기
            Slice<Product> responseProductDtoSlice = productRepository.findByKeyword(keyword, pageable);
            String json = objectMapper.writeValueAsString(responseProductDtoSlice.getContent());
            redisTemplate.opsForValue().set("PopularProducts" + keyword + ":slice:0", json, Duration.ofHours(4));
        }

        //then
        for (String keyword : result) {
            Object cached = redisTemplate.opsForValue().get("PopularProducts" + keyword + ":slice:0");
            savedPopularProducts.add(cached);
        }

        log.info(savedPopularProducts.toString());
        assertThat(savedPopularProducts).isNotEmpty();
    }
}
