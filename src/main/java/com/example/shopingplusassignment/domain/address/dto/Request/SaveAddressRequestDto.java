package com.example.shopingplusassignment.domain.address.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAddressRequestDto {


	@NotBlank (message = "주소를 입력해주세요.")
	private String address;

	@NotNull (message = "우편번호를 입력해주세요.")
	private Long zipCode;

	@NotBlank (message = "수취인명을 입력해주세요.")
	private String recipient;

	@NotBlank (message = "수취인의 휴대폰 번호를 입력해주세요.")
	private String recipientNumber;

	private String deliveryDescription;

	@Setter
	private Boolean isDefaultAddress;



}
