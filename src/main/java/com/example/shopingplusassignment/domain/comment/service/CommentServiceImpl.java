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
import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.order.repository.OrderRepository;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;

import error.ExceptionCode;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;


	@Transactional
	@Override
	public CommentResponseDto saveComment(Long orderId, Long productId, Long userId, CommentRequestDto dto) {

		Order order = orderRepository.findById(orderId).orElseThrow();
        if(!order.getUser().getId().equals(userId)){
			throw  new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_REVIEW_ACCESS);
		}
		if(order.getOrderStatus().equals(OrderStatus.PENDING)){
			throw  new CustomRuntimeException(ExceptionCode.PAYMENT_REQUIRED);
		}
		boolean notFound = order.getProductOrderList()
			.stream()
			.noneMatch(po -> po.getProductId().equals(productId));
		if(notFound){
			throw  new CustomRuntimeException(ExceptionCode.PRODUCTORDER_NOT_FOUND);
		}
		Product findByProduct = productRepository.findById(productId).orElseThrow(()-> new CustomRuntimeException(ExceptionCode.PRODUCT_CANT_FIND));
		Comment Comment = new Comment(dto, order.getUser(),findByProduct);
		Comment saveComment = commentRepository.save(Comment);
		return new CommentResponseDto(saveComment);
	}


	@Transactional(readOnly = true)
	@Override
	public List<CommentGetInfoDto> getCommentByRating(Long productId, int min, int max, int page, int size) {

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creatTime"));

		Page<CommentGetInfoDto> commentResponseDtos = commentRepository.findCommentsByDynamicCondition(productId, min,  max, pageRequest);

		return  commentResponseDtos.getContent();
	}


	@Transactional
	@Override
	public CommentMessageResponseDto deleteComment(Long productId, Long userId, Long reviewId) {

		Comment getComment = commentRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(()-> new CustomRuntimeException(ExceptionCode. COMMENT_NOT_FOUND));

		if (!getComment.getUser().getId().equals(userId)) {
			throw new CustomRuntimeException(ExceptionCode. UNAUTHORIZED_COMMENT_DELETE);
		}

		getComment.markAsDeleted(true, LocalDateTime.now());

		return new CommentMessageResponseDto("정상처리", "정상적으로 삭제 되었습니다.");
	}
}
