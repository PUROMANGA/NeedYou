package com.example.shopingplusassignment.domain.comment.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentCacheService {
	private final RedisTemplate<String, Object> redisTemplate;

	// 1. 좋아요 수 단건 조회
	public long getLikeCount(Long commentId) {
		Object raw = redisTemplate.opsForHash().get("comment:likes", commentId.toString());
		return raw == null ? 0L : Long.parseLong(raw.toString());
	}

	// 2. 좋아요 수 여러 개 조회
	public Map<Long, Long> getLikeCounts(List<Long> commentIds) {
		HashOperations<String, String, Long> hashOps = redisTemplate.opsForHash();
		List<String> keys = commentIds.stream().map(String::valueOf).toList();
		List<Long> rawCounts = hashOps.multiGet("comment:likes", keys);

		Map<Long, Long> result = new HashMap<>();
		for (int i = 0; i < commentIds.size(); i++) {
			Object raw = rawCounts.get(i);
			long count = raw == null ? 0L : Long.parseLong(raw.toString());
			result.put(commentIds.get(i), count);
		}

		return result;
	}

	// 3. 댓글 수 조회
	public long getReviewCount(Long productId) {
		Object raw = redisTemplate.opsForHash().get("product:review:count", productId.toString());
		return raw == null ? 0L : Long.parseLong(raw.toString());
	}

	// 4. 댓글 수 증가
	public void incrementReviewCount(Long productId) {
		redisTemplate.opsForHash().increment("product:review:count", productId.toString(), 1);
	}

	// 5. 댓글 수 감소
	public void decrementReviewCount(Long productId) {
		redisTemplate.opsForHash().increment("product:review:count", productId.toString(), -1);
	}

	// 6. 좋아요 +1
	public void likeComment(Long commentId) {
		redisTemplate.opsForHash().increment("comment:likes", commentId.toString(), 1);
	}

	// 7. 좋아요 취소
	public void unlikeComment(Long commentId) {
		String key = "comment:likes";
		String field = commentId.toString();

		Object raw = redisTemplate.opsForHash().get(key, field);
		long count = raw == null ? 0L : Long.parseLong(raw.toString());

		if (count == 1) {
			redisTemplate.opsForHash().delete(key, field);
		} else if (count > 1) {
			redisTemplate.opsForHash().increment(key, field, -1);
		}
	}
}
