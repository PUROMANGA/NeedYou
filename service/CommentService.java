package com.example.shopingplusassignment.domain.comment.service;

import java.util.List;

import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;

public interface CommentService {
       CommentResponseDto saveComment(Long orderId, Long userId, CommentRequestDto dto);
	   List<CommentResponseDto> getCommentByRating(int minRating, int maxRating , int page, int size);
	   CommentMessageResponseDto delete(Long orderId, Long userId);
}
