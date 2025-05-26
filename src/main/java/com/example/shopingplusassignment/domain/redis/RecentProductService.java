package com.example.shopingplusassignment.domain.redis;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentProductService {

	private static final String USER_VIEW_PRODUCTS = "user:%s:view_items";
	private static final long SECONDS_IN_A_DAY = 1L * 24 * 60 * 60; // 1일
	private static final int MAX_RECENT_PRODUCTS = 5; // 5개만 보세요

	private final RedisTemplate<String,String> redisTemplate;

	// 유저별로 아이템의 id , 조회 시간 저장 (ZSet 사용)
	public void saveRecentProduct(Long userId, Long productId) {
		String key = getUserViewKey(userId);
		double score = getCurrentTimeInSeconds();

		redisTemplate.opsForZSet().add(key, String.valueOf(productId), score);
		// redisTemplate.opsForZSet().remove(key, String.valueOf(productId));
		// redisTemplate.opsForZSet().add(key, String.valueOf(productId), score);
		// redisTemplate.expire(key, java.time.Duration.ofSeconds(SECONDS_IN_A_DAY));


		// 최대갯수 초과시?
		Long size = redisTemplate.opsForZSet().zCard(key);
		if (size != null && size > MAX_RECENT_PRODUCTS) {
			redisTemplate.opsForZSet().removeRange(key, 0, size - MAX_RECENT_PRODUCTS - 1);
		}
	}

	// 최근 7일동안 count 갯수만큼 조회
	public Set<Long> getRecentProduct(Long userId, int count) {
		String key = getUserViewKey(userId);
		double minScore = getCurrentTimeInSeconds() - SECONDS_IN_A_DAY;

		Set<String> result = redisTemplate.opsForZSet()
			.reverseRangeByScore(key, minScore, Double.MAX_VALUE, 0, count);

		if (result == null) return Set.of();

		return convertSet(result);
	}

	private Set<Long> convertSet(Set<String> productIds) {
		return productIds.stream()
			.map(productId -> Long.parseLong((String) productId))
			.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	// userId로 redis 키 생성
	public String getUserViewKey(Long userId) {
		return String.format(USER_VIEW_PRODUCTS, userId);
	}

	// 현재 시간을 가져오는데, 밀리초 -> 초단위 변환
	public double getCurrentTimeInSeconds() {
		return System.currentTimeMillis() / 1000.0;
	}

}
