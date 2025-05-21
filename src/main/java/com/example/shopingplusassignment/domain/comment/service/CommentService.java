package com.example.shopingplusassignment.domain.comment.service;

import java.util.List;

import com.example.shopingplusassignment.domain.comment.dto.CommentGetInfoDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;

public interface CommentService {
       CommentResponseDto saveComment(Long orderId, Long productId, Long userId, CommentRequestDto dto);
	   List<CommentGetInfoDto> getCommentByRating( int min, int max , int page, int size);
	   CommentMessageResponseDto deleteComment(Long orderId, Long productId, Long userId, Long reviewId);
}
