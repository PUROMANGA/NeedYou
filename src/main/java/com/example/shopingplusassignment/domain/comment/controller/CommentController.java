package com.example.shopingplusassignment.domain.comment.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.shopingplusassignment.domain.comment.dto.CommentGetCountResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentGetInfoDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.repository.CommentRepository;
import com.example.shopingplusassignment.domain.comment.service.CommentCacheService;
import com.example.shopingplusassignment.domain.comment.service.CommentServiceImpl;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class CommentController {
	private final CommentServiceImpl commentService;
	private final CommentCacheService commentCacheService;

	@PostMapping
	public ResponseEntity<CommentResponseDto> saveComment(
		@RequestBody CommentRequestDto dto,
		@RequestParam Long orderId,
		@RequestParam Long productId,
		@AuthenticationPrincipal AuthUser authUser){
		return new ResponseEntity<>(commentService.saveComment(orderId, productId, authUser.getUser().getId(), dto), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<CommentGetInfoDto>> findByCommentRating(
		@RequestParam Long productId,
		@RequestParam(defaultValue = "0")int min,
		@RequestParam(defaultValue = "5") int max,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return new ResponseEntity<>(commentService.getCommentByRating(productId, min, max, page, size), HttpStatus.OK);
	}
	@GetMapping("/count")
	public ResponseEntity<CommentGetCountResponseDto> getReviewCount(@RequestParam Long productId) {
		Long count = commentCacheService.getReviewCount(productId);
		CommentGetCountResponseDto commentGetCountResponseDto = new CommentGetCountResponseDto(productId, count);

		return new ResponseEntity<>(commentGetCountResponseDto , HttpStatus.OK);
	}
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<CommentMessageResponseDto> DeleteComment(
		@RequestParam Long productId,
		@PathVariable Long reviewId,
		@AuthenticationPrincipal AuthUser authUser
	){
		return new ResponseEntity<>(commentService.deleteComment( productId, authUser.getUser().getId(), reviewId), HttpStatus.OK);
	}
}
