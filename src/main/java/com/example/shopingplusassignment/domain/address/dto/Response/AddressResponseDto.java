package com.example.shopingplusassignment.domain.address.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponseDto {

	private Long addressId;
	private String address;
	private Boolean isDefaultAddress;
}
