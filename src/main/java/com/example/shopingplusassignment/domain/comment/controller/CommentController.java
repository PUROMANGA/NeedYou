package com.example.shopingplusassignment.domain.comment.controller;

import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.shopingplusassignment.domain.comment.dto.CommentGetCountResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentLikeResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.service.CommentCacheServiceImpl;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;

@Validated
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class CommentController {
	private final CommentCacheServiceImpl commentCacheService;

	/**
	 *  댓글 생성 api
	 *  검증 항목 : 주문 정보, 주문정보내 상품 매칭, 실제 주문한 사람 후 저장.
	 * @param dto 제목, 내용, 별점 추가
	 * @param orderId
	 * @param productId
	 * @param authUser
	 * @return 저장시간 포함해서 출력.
	 */
	@PostMapping
	public ResponseEntity<CommentResponseDto> saveComment(
		@Valid @RequestBody CommentRequestDto dto,
		@RequestParam Long orderId,
		@RequestParam Long productId,
		@AuthenticationPrincipal AuthUser authUser){
		return new ResponseEntity<>(commentCacheService.saveCommentCache(orderId, productId, authUser.getUser().getId(), dto), HttpStatus.OK);
	}

	/**
	 * 특정 상품의 리뷰를 별점 범위 별로 조회
	 * 검증 항목: 상품 Id 를 포함한 리뷰 확인 후 출력
	 * 별점 범위는 0~5까지
	 * @param productId
	 * @param min
	 * @param max
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<CommentResponseDto>> findByCommentRating(
		@RequestParam Long productId,
		@RequestParam(defaultValue = "0")Long min,
		@RequestParam(defaultValue = "5") Long max,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return new ResponseEntity<>(commentCacheService.getCommentByRatingCache(productId, min, max, page, size), HttpStatus.OK);
	}

	@GetMapping("/counts")
	public ResponseEntity<CommentGetCountResponseDto> getReviewCount(
		@RequestParam Long productId
	) {
		return new ResponseEntity<>(commentCacheService.getCommentCountCache(productId), HttpStatus.OK);
	}

	@PatchMapping("/{reviewId}")
	public ResponseEntity<CommentResponseDto> updateComment(
		@Valid @RequestBody CommentRequestDto dto,
		@PathVariable Long reviewId,
		@AuthenticationPrincipal AuthUser authUser
	){
      return new ResponseEntity<>(commentCacheService.updateCommentCache(authUser.getUser().getId(),reviewId,dto),HttpStatus.OK);
	}

	@PatchMapping("/{reviewId}/likes")
	public ResponseEntity<CommentLikeResponseDto> updateLikeComment(
		@RequestParam boolean status,
		@PathVariable Long reviewId,
		@AuthenticationPrincipal AuthUser authUser
	){
		return new ResponseEntity<>(commentCacheService.updateCommentLikeStatusCache(authUser.getUser().getId(),reviewId,status), HttpStatus.OK);
	}

	/**
	 * 유저가 작성한 리뷰를 삭제
	 * 검증 항목: 리뷰가 존해 하고, 본인이 작성한것이 맞는지
	 * @param productId
	 * @param reviewId
	 * @param authUser
	 * @return
	 */
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<CommentMessageResponseDto> DeleteComment(
		@RequestParam Long productId,
		@PathVariable Long reviewId,
		@AuthenticationPrincipal AuthUser authUser
	){
		return new ResponseEntity<>(commentCacheService.deleteCommentCache( productId, authUser.getUser().getId(), reviewId), HttpStatus.OK);
	}
}
