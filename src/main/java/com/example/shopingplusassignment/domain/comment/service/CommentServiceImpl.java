package com.example.shopingplusassignment.domain.comment.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

	private CommentRepository commentRepository;
	@Override
	public CommentResponseDto saveComment(Long orderId, Long userId, CommentRequestDto dto) {
		return null;
	}

	@Override
	public List<CommentResponseDto> getCommentByRating(Long minRating, Long maxRating, Long page, Long size) {
		return List.of();
	}

	@Override
	public CommentMessageResponseDto delete(Long orderId, Long userId) {
		return null;
	}
}
