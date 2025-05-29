package com.example.shopingplusassignment.domain;

import com.example.shopingplusassignment.domain.product.ProductCache;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")

public class ProductCacheTest {

    final Logger log = LoggerFactory.getLogger(ProductCacheTest.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductCache productCache;

    @AfterEach
    void logOutPut() {
        Double score1 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber1");
        Double score2 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber2");
        Double score3 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber3");

        log.info("keyword1 점수 : {}", score1);
        log.info("keyword2 점수 : {}", score2);
        log.info("keyword3 점수 : {}", score3);
    }


    @BeforeEach
    public void clearRedis() {
        log.info("레디스 리셋합니다.");
        redisTemplate.delete("SearchKeywords");
        redisTemplate.delete("SearchKeywords");

        String email = "test@email.com";
        String[] emails = new String[350];

        for(int i = 0; i < 200; i ++) {
            emails[i] = "test" + i + email;
            String keywordNumber1 = "testKeywordNumber1";
            String userValue = "userEmail" + ":" + emails[i] + "," + "Keyword" + keywordNumber1;
            redisTemplate.delete(userValue);
        }

        for(int i = 200; i < 300; i ++) {
            emails[i] = "test" + i + "email";
            String keywordNumber2 = "testKeywordNumber2";
            String userValue = "userEmail" + ":" + emails[i] + "," + "Keyword" + keywordNumber2;
            redisTemplate.delete(userValue);
        }

        for(int i = 300; i < 350; i ++) {
            emails[i] = "test" + i + "email";
            String keywordNumber3 = "testKeywordNumber3";
            String userValue = "userEmail" + ":" + emails[i] + "," + "Keyword" + keywordNumber3;
            redisTemplate.delete(userValue);
        }
    }

    /**
     * savedKeywordByCounting 테스트
     */

    @Test
    @DisplayName("캐시에 email-keyword가 없으면 저장하고 점수 증가")
    public void testSavedKeywordByCounting() {
        // given
        String email = "test@email.com";
        String[] emails = new String[350];

        // when
        for(int i = 0; i < 200; i ++) {
            emails[i] = "test" + i + email;
            String keywordNumber1 = "testKeywordNumber1";
            productCache.savedKeywordByCounting(keywordNumber1, emails[i]);
        }

        for(int i = 200; i < 300; i ++) {
            emails[i] = "test" + i + "email";
            String keywordNumber2 = "testKeywordNumber2";
            productCache.savedKeywordByCounting(keywordNumber2, emails[i]);
        }

        for(int i = 300; i < 350; i ++) {
            emails[i] = "test" + i + "email";
            String keywordNumber3 = "testKeywordNumber3";
            productCache.savedKeywordByCounting(keywordNumber3, emails[i]);
        }

        Double score1 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber1");
        Double score2 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber2");
        Double score3 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber3");

        //then
        assertThat(score1).isEqualTo(200);
        assertThat(score2).isEqualTo(100);
        assertThat(score3).isEqualTo(50);
    }


    /**
     * savedKeywordByCounting 테스트
     */

    @Test
    @DisplayName("어뷰징 방지 테스트")
    public void testCantCompareEmailSavedKeyword() {
        // given
        String email = "test@email.com";
        String[] emails = new String[350];

        // when
        for(int i = 0; i < 200; i ++) {
            emails[i] = "test" + i + email;
            String keywordNumber1 = "testKeywordNumber1";
            productCache.savedKeywordByCounting(keywordNumber1, emails[i]);
        }

        for(int i = 200; i < 300; i ++) {
            String keywordNumber2 = "testKeywordNumber2";
            productCache.savedKeywordByCounting(keywordNumber2, email);
        }

        for(int i = 300; i < 350; i ++) {
            String keywordNumber3 = "testKeywordNumber3";
            productCache.savedKeywordByCounting(keywordNumber3, email);
        }

        Double score1 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber1");
        Double score2 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber2");
        Double score3 = redisTemplate.opsForZSet().score("SearchKeywords", "testKeywordNumber3");

        //then
        assertThat(score1).isEqualTo(200);
        assertThat(score2).isEqualTo(null);
        assertThat(score3).isEqualTo(null);
    }
}
