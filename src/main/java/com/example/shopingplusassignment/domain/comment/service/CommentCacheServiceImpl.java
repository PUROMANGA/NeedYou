package com.example.shopingplusassignment.domain.comment.service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.example.shopingplusassignment.domain.comment.dto.CommentGetCountResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.entity.Comment;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentCacheServiceImpl implements CommentCacheService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final CommentService commentService;

	/**
	 *
	 * @param orderId
	 * @param productId
	 * @param userId
	 * @param dto
	 * @return
	 */
	@Override
	public CommentResponseDto saveCommentCache(Long orderId, Long productId, Long userId, CommentRequestDto dto) {
		CommentResponseDto commentResponseDto = commentService.saveComment( orderId, productId, userId, dto);
		invalidateCommentCache(productId); // 목록 무효화
		invalidateReviewCount(productId); // 카운트 무효화
		invalidateLikeCount(commentResponseDto.getId());    // 좋아요 무효화
		return commentResponseDto;
	}



	/**
	 *
	 * @param productId
	 * @param userId
	 * @param reviewId
	 * @return
	 */
	@Override
	public CommentMessageResponseDto deleteCommentCache(Long productId, Long userId, Long reviewId) {
		CommentMessageResponseDto commentMessageResponseDto = commentService.deleteComment(productId, userId, reviewId);
		invalidateCommentCache(productId); // 목록 무효화
		invalidateReviewCount(productId); // 카운트 무효화
		invalidateLikeCount(reviewId);    // 좋아요 무효화
		return commentMessageResponseDto;
	}

	/**
	 *
	 * @param userId
	 * @param reviewId
	 * @param requestDto
	 * @return
	 */
	@Override
	public CommentResponseDto updateCommentCache(Long userId, Long reviewId, CommentRequestDto requestDto) {
		CommentResponseDto commentResponseDto = commentService.updateComment(userId,reviewId,requestDto);

		Long productId = commentResponseDto.getProductId();

		invalidateCommentCache(productId); // 목록 무효화
		invalidateReviewCount(productId); // 카운트 무효화
		invalidateLikeCount(reviewId);    // 좋아요 무효화
		return commentResponseDto ;
	}

	/**
	 *
	 * @param productId
	 * @param min
	 * @param max
	 * @param page
	 * @param size
	 * @return
	 */
	@Cacheable(
		value = "comment",
		key = "'product:' + #productId + ':min:' + #min + ':max:' + #max + ':page:' + #page",
		cacheManager = "redisCacheManager"
	)
	@Override
	public List<CommentResponseDto> getCommentByRatingCache(
		Long productId,
		Long min,
		Long max,
		int page,
		int size) {

		long start = System.currentTimeMillis();
		List<CommentResponseDto> getInfoResponseDtoList =
			commentService.getCommentByRating(productId,min, max,  page, size);

		long end = System.currentTimeMillis();
		log.info("캐싱 조회 WD시간: {}ms",(end - start));
		return getInfoResponseDtoList;
	}

	/**
	 *
	 * @param productId
	 * @return
	 */
	@Override
	public CommentGetCountResponseDto getCommentCountCache(Long productId) {
		Object cached = redisTemplate.opsForHash().get("product:review:count", productId.toString());
		Long count;

		if (cached != null) {
			count = Long.parseLong(cached.toString());
		} else {
			count = commentService.getCommentCount(productId);
			redisTemplate.opsForHash().put("product:review:count", productId.toString(), count);
			redisTemplate.expire("product:review:count", Duration.ofMinutes(3));
		}
		return new CommentGetCountResponseDto(productId, count);
	}

	/**
	 * 목록 무효화
	 * @param userId
	 * @param reviewId
	 * @param status
	 * @return
	 */
	@Override
	public CommentMessageResponseDto updateCommentLikeStatusCache(Long userId, Long reviewId, boolean status) {
		Comment comment = commentService.applyLikeStatus(userId, reviewId, status);
		redisTemplate.opsForHash().put("comment:likes", comment.getId().toString(), comment.getLikeCount());
		return new CommentMessageResponseDto("성공", status ? "좋아요가 반영되었습니다." : "좋아요가 취소되었습니다.");
	}

	// TODO: 이 메서드들은 내부 전용. 여유 있을 때 private 전환 고려.
	/**
	 *
	 * @param productId
	 */
	@Override
	public void invalidateCommentCache(Long productId) {
		Set<String> keys = redisTemplate.keys("comment::product:" + productId + ":page:*");
		redisTemplate.delete(keys);
	}

	/**
	 * 카운트 무효화
	 * @param productId
	 */
	@Override
	public void invalidateReviewCount(Long productId) {
		redisTemplate.opsForHash().delete("product:review:count", productId.toString());
	}

	/**
	 * 좋아요 무효화
	 * @param commentId
	 */
	@Override
	public void invalidateLikeCount(Long commentId) {
		redisTemplate.opsForHash().delete("comment:likes", commentId.toString());
	}
}
