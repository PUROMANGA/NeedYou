package com.example.shopingplusassignment.domain.comment.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;


	@Transactional
	@Override
	public CommentResponseDto saveComment(Long orderId, Long productId, Long userId, CommentRequestDto dto) {

		Order order = orderRepository.findById(orderId).orElseThrow();


		Comment Comment = new Comment(dto, order);
		Comment saveComment = commentRepository.save(Comment);
		return new CommentResponseDto(saveComment);
	}


	@Transactional(readOnly = true)
	@Override
	public List<CommentGetInfoDto> getCommentByRating( int min, int max, int page, int size) {

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creatTime"));

		Page<CommentGetInfoDto> commentResponseDtos = commentRepository.findCommentsByDynamicCondition( min,  max, pageRequest);

		commentResponseDtos.getContent().forEach(dto -> System.out.println("💬 " + dto));

		return  commentResponseDtos.getContent();
	}

	/**
	 *
	 * @param orderId
	 * @param productId
	 * @param userId
	 * @param reviewId
	 * @return
	 */
	@Transactional
	@Override
	public CommentMessageResponseDto deleteComment(Long orderId, Long productId, Long userId, Long reviewId) {

		Comment getComment = commentRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(()-> new RuntimeException("리뷰가 존재하지 않습니다."));

		if (!getComment.getUser().getId().equals(userId)) {
			throw new RuntimeException("본인이 작성한 댓글만 삭제할 수 있습니다.");
		}

		getComment.markAsDeleted(true, LocalDateTime.now());

		return new CommentMessageResponseDto("정상처리", "정상적으로 삭제 되었습니다.");
	}
}
