package com.example.shopingplusassignment.domain.comment.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequestDto {

	@NotBlank(message = "제목은 비워둘 수 없습니다.")
	private String title;

	@NotBlank(message = "내용은 비워둘 수 없습니다.")
	private String description;

	@NotNull(message = "평점은 필수 입력 항목입니다.")
	@Min(value = 0, message = "평점은 최소 0 이상이어야 합니다.")
	@Max(value = 5, message = "평점은 최대 5 이하여야 합니다.")
	int rating;

	public CommentRequestDto(String title, String description, int rating) {
		this.title = title;
		this.description = description;
		this.rating = rating;
	}


}
