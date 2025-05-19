package com.example.shopingplusassignment.domain.comment.service;

import java.util.List;

import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;

public interface CommentService {
       CommentResponseDto saveComment(Long orderId, Long userId, CommentRequestDto dto);
	   List<CommentResponseDto> getCommentByRating(Long minRating, Long maxRating , Long page, Long size);
	   CommentMessageResponseDto delete(Long orderId, Long userId);
}
