package com.example.shopingplusassignment.domain.address.dto.Response;

import com.example.shopingplusassignment.domain.address.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailAddressResponseDto {

	private String address;

	private Long zipCode;

	private String recipient;

	private String recipientNumber;

	private String deliveryDescription;

	private Boolean isDefaultAddress;

	public static DetailAddressResponseDto toDto (Address address) {
		return new DetailAddressResponseDto(
			address.getAddress(),
			address.getZipCode(),
			address.getRecipient(),
			address.getRecipientNumber(),
			address.getDeliveryDescription(),
			address.getIsDefaultAddress()
		);
	}

}
