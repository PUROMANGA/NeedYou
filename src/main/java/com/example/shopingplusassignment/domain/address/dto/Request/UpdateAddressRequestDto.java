package com.example.shopingplusassignment.domain.address.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressRequestDto {

	private String address;

	private Long zipCode;

	private String recipient;

	private String recipientNumber;

	private String deliveryDescription;

	private Boolean isDefaultAddress;
}
