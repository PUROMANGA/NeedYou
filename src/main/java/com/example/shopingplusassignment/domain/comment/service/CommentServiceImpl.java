package com.example.shopingplusassignment.domain.comment.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.shopingplusassignment.error.CustomRuntimeException;
import com.example.shopingplusassignment.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.order.repository.OrderRepository;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	/**
	 * 댓글 저장 서비스
	 *
	 * 검증 항목:
	 * - 해당 주문이 존재하는지
	 * - 주문한 사용자가 현재 로그인 사용자와 일치하는지
	 * - 결제가 완료된 주문인지 확인
	 * - 주문에 해당 상품이 포함되어 있는지
	 *
	 * @param orderId   주문 ID
	 * @param productId 상품 ID
	 * @param userId    사용자 ID
	 * @param dto       댓글 요청 DTO
	 * @return          저장된 댓글 응답 DTO
	 */
	@Transactional
	@Override
	public CommentResponseDto saveComment(Long orderId, Long productId, Long userId, CommentRequestDto dto) {

		Order order = orderRepository.findById(orderId).orElseThrow(()
			-> new CustomRuntimeException(ExceptionCode.ORDER_NOT_FOUND));
        if(!order.getUser().getId().equals(userId)){
			//System.out.println(""+userId + "user" + userId);
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

	/**
	 * 상품별 평점 범위에 따른 댓글 조회 (페이지네이션 포함)
	 *
	 * @param productId 상품 ID
	 * @param min       평점 최소값
	 * @param max       평점 최대값
	 * @param page      페이지 번호
	 * @param size      페이지당 항목 수
	 * @return          조건에 부합하는 댓글 목록
	 */
    @Transactional(readOnly = true)
	@Override
	public List<CommentResponseDto> getCommentByRating(Long productId, Long min, Long max, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creatTime"));
		Page<CommentResponseDto> commentResponseDtos = commentRepository.findCommentsByDynamicCondition(productId, min,  max, pageRequest);
		return  commentResponseDtos.getContent();
	}
	/**
	 * 특정 상품의 댓글 수 조회
	 *
	 * @param productId 상품 ID
	 * @return          댓글 수
	 */
	@Override
	public Long getCommentCount(Long productId) {
		return commentRepository.countByProductIdAndDeletedAtIsNull(productId);
	}


	/**
	 * 댓글 수정 처리
	 *
	 * 검증 항목:
	 * - 해당 댓글이 존재하는지
	 * - 수정 요청자가 댓글 작성자인지 확인
	 *
	 * @param userId     사용자 ID
	 * @param reviewId   댓글 ID
	 * @param requestDto 수정 요청 DTO
	 * @return           수정된 댓글 정보 DTO
	 */
	@Transactional
	@Override
	public CommentResponseDto updateComment(Long userId, Long reviewId, CommentRequestDto requestDto) {
		Comment getComment = commentRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(()-> new CustomRuntimeException(ExceptionCode. COMMENT_NOT_FOUND));
		if (!getComment.getUser().getId().equals(userId)) {
			throw new CustomRuntimeException(ExceptionCode. UNAUTHORIZED_COMMENT_DELETE);
		}
		getComment.updateComment(requestDto.getTitle(), requestDto.getDescription(),requestDto.getRating());
		Comment changeComment = commentRepository.save(getComment);
		return new CommentResponseDto(changeComment);
	}
	/**
	 * 댓글 좋아요 상태 변경 처리
	 *
	 * 검증 항목:
	 * - 댓글이 존재하는지 확인
	 * - 상태에 따라 좋아요 수 증가 또는 감소
	 *
	 * @param userId   사용자 ID
	 * @param reviewId 댓글 ID
	 * @param status   true: 좋아요, false: 취소
	 * @return         업데이트된 댓글 엔티티
	 */
	@Transactional
	@Override
	public Comment applyLikeStatus(Long userId, Long reviewId, boolean status) {
		Comment getComment = commentRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(()-> new CustomRuntimeException(ExceptionCode. COMMENT_NOT_FOUND));

	    getComment.increaseLikeCount(status);
		return getComment;
	}

	/**
	 * 댓글 삭제 처리 (Soft delete 방식)
	 *
	 * 검증 항목:
	 * - 해당 댓글이 존재하는지
	 * - 요청 사용자가 댓글 작성자인지 확인
	 *
	 * @param productId 상품 ID
	 * @param userId    사용자 ID
	 * @param reviewId  댓글 ID
	 * @return          삭제 처리 메시지 DTO
	 */
	@Transactional
	@Override
	public CommentMessageResponseDto deleteComment(Long productId, Long userId, Long reviewId) {

		Comment getComment = commentRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(()-> new CustomRuntimeException(ExceptionCode. COMMENT_NOT_FOUND));

		if (!getComment.getUser().getId().equals(userId)) {
			throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_COMMENT_UPDATE);
		}

		getComment.markAsDeleted(true, LocalDateTime.now());
		return new CommentMessageResponseDto("정상처리", "정상적으로 삭제 되었습니다.");
	}
}
