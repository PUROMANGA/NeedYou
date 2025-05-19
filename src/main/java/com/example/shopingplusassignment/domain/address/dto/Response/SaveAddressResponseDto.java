package com.example.shopingplusassignment.domain.address.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveAddressResponseDto {

	private String message;
	private DetailAddressResponseDto address;
}
