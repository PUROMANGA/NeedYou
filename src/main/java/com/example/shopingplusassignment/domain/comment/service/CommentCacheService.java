package com.example.shopingplusassignment.domain.comment.service;

import java.util.List;

import com.example.shopingplusassignment.domain.comment.dto.CommentGetCountResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentLikeResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;

public interface CommentCacheService {


	CommentResponseDto saveCommentCache(Long orderId, Long productId, Long userId, CommentRequestDto dto);

	CommentMessageResponseDto deleteCommentCache(Long productId, Long userId, Long reviewId);

	CommentResponseDto updateCommentCache(Long userId, Long reviewId, CommentRequestDto requestDto);

	List<CommentResponseDto> getCommentByRatingCache(Long productId, Long min, Long max, int page, int size);

	CommentGetCountResponseDto getCommentCountCache(Long productId);

	CommentLikeResponseDto updateCommentLikeStatusCache(Long userId, Long reviewId, boolean status);

	void invalidateCommentCache(Long productId);

	void invalidateReviewCount(Long productId);

	void invalidateLikeCount(Long commentId);

}
