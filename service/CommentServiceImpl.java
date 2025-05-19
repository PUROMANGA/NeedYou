package com.example.shopingplusassignment.domain.comment.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.entity.Comment;
import com.example.shopingplusassignment.domain.comment.repository.CommentRepository;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;

	@Transactional
	@Override
	public CommentResponseDto saveComment(Long orderId, Long userId, CommentRequestDto dto) {

		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
		if (order.getUser().getId().equal(userId)) {
			new RuntimeException("주문자와 일치하지 않습니다.");
		}
		Comment getComment = new Comment(dto, order);
		Comment saveComment = commentRepository.save(getComment);
		return new CommentResponseDto(saveComment);
	}

	@Transactional
	@Override
	public List<CommentResponseDto> getCommentByRating(int minRating, int maxRating, int page, int size) {

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Comment> comments = commentRepository.findCommentByRatingBetween(minRating, maxRating, pageRequest);
		return comments.stream().map(CommentResponseDto::new).toList();
	}

	@Transactional
	@Override
	public CommentMessageResponseDto delete(Long orderId, Long userId, Long reviewId) {
		Order order = orderRepository.findById(orderId).ElseOrThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
		if (order.getUser().getId().equal(userId)) {
			new RuntimeException("주문자와 일치하지 않습니다.");
		}
		Comment getComment = commentRepository.findById(reviewId).orElseThrow(()-> new RuntimeException("리뷰가 존재하지 않습니다."));
		return new CommentMessageResponseDto("정상처리", "정상적으로 삭제 되었습니다.");
	}
}
