package com.example.shopingplusassignment.domain.comment.service;

import error.CustomRuntimeException;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shopingplusassignment.domain.comment.dto.CommentGetInfoDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.entity.Comment;
import com.example.shopingplusassignment.domain.comment.repository.CommentRepository;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.order.repository.OrderRepository;

import error.ExceptionCode;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final OrderRepository orderRepository;


	@Transactional
	@Override
	public CommentResponseDto saveComment(Long orderId, Long productId, Long userId, CommentRequestDto dto) {

		Order order = orderRepository.findById(orderId).orElseThrow();
        if(!order.getUser().getId().equals(userId)){
			throw  new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_REVIEW_ACCESS);
		}
		boolean notFound =order.getProductOrderList()
			.stream()
			.noneMatch(productOrder -> !productOrder.getProductId().equals(productId));
		if(notFound){
			throw  new CustomRuntimeException(ExceptionCode.PRODUCTORDER_NOT_FOUND);
		}

		Comment Comment = new Comment(dto, order);
		Comment saveComment = commentRepository.save(Comment);
		return new CommentResponseDto(saveComment);
	}


	@Transactional(readOnly = true)
	@Override
	public List<CommentGetInfoDto> getCommentByRating(Long productId, int min, int max, int page, int size) {

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creatTime"));

		Page<CommentGetInfoDto> commentResponseDtos = commentRepository.findCommentsByDynamicCondition(productId, min,  max, pageRequest);

		commentResponseDtos.getContent().forEach(dto -> System.out.println("💬 " + dto));

		return  commentResponseDtos.getContent();
	}


	@Transactional
	@Override
	public CommentMessageResponseDto deleteComment(Long productId, Long userId, Long reviewId) {

		Comment getComment = commentRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(()-> new RuntimeException("리뷰가 존재하지 않습니다."));

		if (!getComment.getUser().getId().equals(userId)) {
			throw new RuntimeException("본인이 작성한 댓글만 삭제할 수 있습니다.");
		}

		getComment.markAsDeleted(true, LocalDateTime.now());

		return new CommentMessageResponseDto("정상처리", "정상적으로 삭제 되었습니다.");
	}
}
