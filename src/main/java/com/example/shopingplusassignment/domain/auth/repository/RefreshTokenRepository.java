package com.example.shopingplusassignment.domain.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepository {
	// Redis 접근
	private final RedisTemplate<String, String> redisTemplate;
	private final RedisTemplate<String, Object> redisBlackListTemplate;

	// key 값 구분용
	private static final String PREFIX = "RT:";

	// 로그인 성공 -> Redis에 RefreshToken 저장
	public void save(String email, String refreshToken, long expirationMillis) {
		redisTemplate.opsForValue().set(PREFIX + email, refreshToken, Duration.ofMillis(expirationMillis));
	}

	// 재로그인 -> 저장된 토큰을 조회
	public String findByUserEmail(String email) {
		return redisTemplate.opsForValue().get(PREFIX + email);
	}

	// 로그아웃 -> RefreshToken 삭제
	public void delete(String email) {
		redisTemplate.delete(PREFIX + email);
	}

	public void setBlackList(String key, Object o, Long milliSeconds) {
		redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
		redisBlackListTemplate.opsForValue().set(key, o, milliSeconds, TimeUnit.MILLISECONDS);
	}

	public boolean hasKeyBlackList(String key) {
		return redisBlackListTemplate.hasKey(key);
	}
}
