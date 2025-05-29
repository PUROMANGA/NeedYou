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
import com.example.shopingplusassignment.domain.comment.dto.CommentLikeResponseDto;
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
	 * 댓글 저장 후 관련 캐시 무효화
	 *
	 * @param orderId    주문 ID (댓글 작성 검증 용도)
	 * @param productId  상품 ID
	 * @param userId     사용자 ID
	 * @param dto        댓글 작성 요청 DTO
	 * @return           저장된 댓글 정보 DTO
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
	 * 댓글 삭제 후 관련 캐시 무효화
	 *
	 * @param productId  상품 ID
	 * @param userId     사용자 ID
	 * @param reviewId   삭제할 댓글 ID
	 * @return           삭제 결과 메시지 DTO
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
	 * 댓글 수정 후 관련 캐시 무효화
	 *
	 * @param userId      사용자 ID
	 * @param reviewId    댓글 ID
	 * @param requestDto  수정 요청 DTO
	 * @return            수정된 댓글 정보 DTO
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
	 * 평점 범위 필터링 + 페이지 조건에 따라 캐시된 댓글 목록 조회
	 *
	 * @param productId  상품 ID
	 * @param min        평점 최소값
	 * @param max        평점 최대값
	 * @param page       페이지 번호
	 * @param size       페이지당 개수
	 * @return           조건에 해당하는 댓글 목록
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
	 * 상품별 댓글 수 조회 (캐시 우선, 없으면 DB 조회 후 캐싱)
	 *
	 * @param productId 상품 ID
	 * @return          댓글 수 DTO
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
	 * 좋아요 상태 갱신 후 캐시 업데이트 및 결과 반환
	 *
	 * @param userId    사용자 ID
	 * @param reviewId  댓글 ID
	 * @param status    true: 좋아요, false: 좋아요 취소
	 * @return          좋아요 결과 메시지 DTO
	 */
	@Override
	public CommentLikeResponseDto updateCommentLikeStatusCache(Long userId, Long reviewId, boolean status) {
		Comment comment = commentService.applyLikeStatus(userId, reviewId, status);
		Long count =comment.getLikeCount();
		redisTemplate.opsForHash().put("comment:likes", comment.getId().toString(), count);
		String message;

		if (status) {
			message = "좋아요가 반영되었습니다.";
		} else if (count == 0) {
			message = "이미 좋아요가 없습니다.";
		} else {
			message = "좋아요가 취소되었습니다.";
		}

		return new CommentLikeResponseDto("성공", message,  count);
	}

	// TODO: 이 메서드들은 내부 전용. 여유 있을 때 private 전환 고려.
	/**
	 * 댓글 목록 캐시 무효화 (페이징 기반)
	 *
	 * @param productId 상품 ID
	 */
	@Override
	public void invalidateCommentCache(Long productId) {
		Set<String> keys = redisTemplate.keys("comment::product:" + productId + ":page:*");
		redisTemplate.delete(keys);
	}

	/**
	 * 댓글 수 캐시 무효화
	 *
	 * @param productId 상품 ID
	 */
	@Override
	public void invalidateReviewCount(Long productId) {
		redisTemplate.opsForHash().delete("product:review:count", productId.toString());
	}

	/**
	 * 좋아요 수 캐시 무효화
	 *
	 * @param commentId 댓글 ID
	 */
	@Override
	public void invalidateLikeCount(Long commentId) {
		redisTemplate.opsForHash().delete("comment:likes", commentId.toString());
	}
}
