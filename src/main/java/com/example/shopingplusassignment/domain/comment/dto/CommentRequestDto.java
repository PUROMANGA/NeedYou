package com.example.shopingplusassignment.domain.comment.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequestDto {

	@NotBlank
	String title;
	@NotBlank
	String description;

	@Max(value = 5L)
	@Min(value = 0L)
	int rating;

	public CommentRequestDto(String title, String description, int rating) {
		this.title = title;
		this.description = description;
		this.rating = rating;
	}

}
