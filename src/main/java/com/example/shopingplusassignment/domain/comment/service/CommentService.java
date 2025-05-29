package com.example.shopingplusassignment.domain.comment.service;

import java.util.List;

import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.entity.Comment;

public interface CommentService {

       CommentResponseDto saveComment(Long orderId, Long productId, Long userId, CommentRequestDto dto);

	   List<CommentResponseDto> getCommentByRating(Long productId, Long min, Long max, int page, int size);

	   Comment applyLikeStatus(Long userId, Long reviewId, boolean status);

	   Long getCommentCount(Long productId);

	   CommentResponseDto updateComment(Long userId, Long reviewId, CommentRequestDto requestDto);

	   CommentMessageResponseDto deleteComment(Long productId, Long userId, Long reviewId);
}
