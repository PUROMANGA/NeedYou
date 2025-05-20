package com.example.shopingplusassignment.domain.comment.service;

import java.time.LocalDateTime;
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

	/**
	 *  Comment 생성
	 * @param orderId
	 * @param userId
	 * @param dto
	 * @return commentResponse 반환
	 */
	@Transactional
	@Override
	public CommentResponseDto saveComment(Long orderId, Long userId, CommentRequestDto dto) {

		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
		if (!order.getUser().getId().equals(userId)) {
			throw new RuntimeException("주문자와 일치하지 않습니다.");
		}
		Comment Comment = new Comment(dto, order);

		Comment saveComment = commentRepository.save(Comment);

		return new CommentResponseDto(saveComment);
	}

	/**
	 *  삭제되지 않는 별점의 범위내 결과 출력하기.
	 * @param minRating
	 * @param maxRating
	 * @param page
	 * @param size
	 * @return Page 내 응답 리스트 출력
	 */
	@Transactional(readOnly = true)
	@Override
	public List<CommentResponseDto> getCommentByRating(int minRating, int maxRating, int page, int size) {

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

		Page<Comment> comments = commentRepository.findCommentByRatingBetweenAndDeletedAtIsNull(minRating, maxRating, pageRequest);

		return comments.stream().map(CommentResponseDto::new).toList();
	}

	/**
	 * Comment 삭제 , 삭제시 본인거인지 자동으로 확인 후 소프트 Delete 진행
	 * @param orderId
	 * @param userId
	 * @param reviewId
	 * @return 정상처리 메세지 출력
	 */
	@Transactional
	@Override
	public CommentMessageResponseDto deleteComment(Long orderId, Long userId, Long reviewId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));
		if (!order.getUser().getId().equals(userId)) {
			throw new RuntimeException("주문자와 일치하지 않습니다.");
		}


		Comment getComment = commentRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(()-> new RuntimeException("리뷰가 존재하지 않습니다."));

		if (!getComment.getUser().getId().equals(userId)) {
			throw new RuntimeException("본인이 작성한 댓글만 삭제할 수 있습니다.");
		}

		getComment.markAsDeleted(true, LocalDateTime.now());

		return new CommentMessageResponseDto("정상처리", "정상적으로 삭제 되었습니다.");
	}
}
