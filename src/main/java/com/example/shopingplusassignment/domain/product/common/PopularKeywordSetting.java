package com.example.shopingplusassignment.domain.product.common;

import com.example.shopingplusassignment.domain.ProductDocument.ElasticCommonProductRepository;
import com.example.shopingplusassignment.domain.ProductDocument.ProductDocument;
import com.example.shopingplusassignment.domain.product.dto.ResponseProductDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor

public class PopularKeywordSetting {

    private List<String> getTopKeyword = new ArrayList<>();
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ElasticCommonProductRepository elasticCommonProductRepository;



    @Scheduled(cron = "0 0 */4 * * *")
    public void addPopularKeyword() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();


        Set<ZSetOperations.TypedTuple<Object>> topKeywords =
                redisTemplate.opsForZSet()
                        .reverseRangeWithScores("SearchKeywords", 0, 9);

        redisTemplate.expire("SearchKeywords", Duration.ofHours(4));

        List<String> result = topKeywords.stream().map(tuple -> tuple.getValue().toString()).toList();
        this.getTopKeyword = result;

        for (String keyword : getTopKeyword) {
            Pageable pageable = PageRequest.of(0, 10);

            Slice<ProductDocument> productDocuments = elasticCommonProductRepository.searchByKeyword(keyword, pageable);
            String json = objectMapper.writeValueAsString(productDocuments);
            redisTemplate.opsForValue().set("PopularProducts" + keyword + ":slice:0", json, Duration.ofHours(4));
        }
    }

    public List<String> getPopularKeyword() {
        return getTopKeyword;
    }
}
