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
	 * 댓글 작성 API
	 *
	 * 검증 항목:
	 * - 해당 주문 정보가 존재하는지
	 * - 주문 정보 내에 상품이 포함되어 있는지
	 * - 요청 유저가 해당 주문을 실제로 한 사람인지
	 *
	 * @param dto       댓글 요청 DTO (제목, 내용, 평점 포함)
	 * @param orderId   주문 ID
	 * @param productId 상품 ID
	 * @param authUser  인증된 사용자
	 * @return          작성된 댓글 정보 (작성 시각 포함)
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
	 * 특정 상품의 댓글 목록을 평점 범위별로 조회
	 *
	 * 검증 항목:
	 * - 해당 상품 ID가 유효한지 확인
	 * - 평점(min~max)은 0~5 사이 범위로 제한됨
	 *
	 * @param productId 상품 ID
	 * @param min       평점 최소값 (기본값: 0)
	 * @param max       평점 최대값 (기본값: 5)
	 * @param page      페이지 번호 (기본값: 0)
	 * @param size      페이지당 항목 수 (기본값: 10)
	 * @return          조건에 해당하는 댓글 목록
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
	/**
	 * 상품별 댓글 수 조회 API
	 *
	 * @param productId 상품 ID
	 * @return          해당 상품에 대한 총 댓글 수
	 */
	@GetMapping("/counts")
	public ResponseEntity<CommentGetCountResponseDto> getReviewCount(
		@RequestParam Long productId
	) {
		return new ResponseEntity<>(commentCacheService.getCommentCountCache(productId), HttpStatus.OK);
	}
	/**
	 * 댓글 수정 API
	 *
	 * 검증 항목:
	 * - 해당 댓글이 존재하는지
	 * - 수정 요청자가 댓글 작성자인지
	 *
	 * @param dto       수정 요청 DTO (제목, 내용, 평점)
	 * @param reviewId  댓글 ID
	 * @param authUser  인증된 사용자
	 * @return          수정된 댓글 정보
	 */
	@PatchMapping("/{reviewId}")
	public ResponseEntity<CommentResponseDto> updateComment(
		@Valid @RequestBody CommentRequestDto dto,
		@PathVariable Long reviewId,
		@AuthenticationPrincipal AuthUser authUser
	){
      return new ResponseEntity<>(commentCacheService.updateCommentCache(authUser.getUser().getId(),reviewId,dto),HttpStatus.OK);
	}
	/**
	 * 댓글 좋아요/취소 API
	 *
	 * 검증 항목:
	 * - 해당 댓글이 존재하는지
	 * - 사용자가 이전에 좋아요를 눌렀는지 여부
	 *
	 * @param status    true: 좋아요 추가 / false: 좋아요 취소
	 * @param reviewId  댓글 ID
	 * @param authUser  인증된 사용자
	 * @return          좋아요 반영 결과 (상태 메시지, 반영된 좋아요 수 포함)
	 */
	@PatchMapping("/{reviewId}/likes")
	public ResponseEntity<CommentLikeResponseDto> updateLikeComment(
		@RequestParam boolean status,
		@PathVariable Long reviewId,
		@AuthenticationPrincipal AuthUser authUser
	){
		return new ResponseEntity<>(commentCacheService.updateCommentLikeStatusCache(authUser.getUser().getId(),reviewId,status), HttpStatus.OK);
	}

	/**
	 * 댓글 삭제 API
	 *
	 * 검증 항목:
	 * - 해당 댓글이 존재하는지
	 * - 요청자가 해당 댓글의 작성자인지 확인
	 *
	 * @param productId 상품 ID
	 * @param reviewId  댓글 ID
	 * @param authUser  인증된 사용자
	 * @return          삭제 결과 메시지
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
