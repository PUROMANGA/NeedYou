package com.example.shopingplusassignment.domain.comment.control;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.shopingplusassignment.domain.comment.dto.CommentMessageResponseDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.repository.CommentRepository;
import com.example.shopingplusassignment.domain.comment.service.CommentServiceImpl;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {
	private final CommentServiceImpl commentService;
	private final CommentRepository commentRepository;

	@PostMapping
	public ResponseEntity<CommentResponseDto> saveComment(
		@RequestBody CommentRequestDto dto,
		@PathVariable Long orderId,
		@AuthenticationPrincipal UserDeatil userDeatil){
		return new ResponseEntity<>(commentService.saveComment(orderId, userDeatil.getUserId(), dto), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<CommentResponseDto>> findByCommentRating(
		@RequestParam(defaultValue = "0")int min,
		@RequestParam(defaultValue = "5") int max,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {

		return new ResponseEntity<>(commentService.getCommentByRating(min, max, page, size), HttpStatus.NO_CONTENT);
	}

	@DeleteMapping
	public ResponseEntity<CommentMessageResponseDto> DeleteComment(
		@PathVariable Long orderId,
		@PathVariable Long reviewId,
		@AuthenticationPrincipal UserDeatil userDeatil
	){
		return new ResponseEntity<>(commentService.deleteComment(orderId,userDeatil.getuserId(), reviewId));
	}
}
