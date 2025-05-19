package com.example.shopingplusassignment.domain.address.dto.Response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SaveAddressResponseDto {

	private String address;

	private Long zipCode;

	private String recipient;

	private String recipientNumber;

	private String deliveryDescription;

	private boolean isDefaultAddress;

}
