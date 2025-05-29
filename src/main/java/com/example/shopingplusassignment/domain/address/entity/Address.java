package com.example.shopingplusassignment.domain.address.entity;

import com.example.shopingplusassignment.domain.address.dto.Request.UpdateAddressRequestDto;
import com.example.shopingplusassignment.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table (name = "address")
@NoArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Setter
	@Column (nullable = false)
	private Long zipCode;

	@Setter
	@Column (nullable = false)
	private String address;

	@Setter
	@Column (nullable = false)
	private String recipient;

	@Setter
	@Column (nullable = false)
	private String recipientNumber;

	@Setter
	private String deliveryDescription;

	@Setter
	@Column (nullable = false)
	private Boolean isDefaultAddress;

	public Address(User user, String address, Long zipCode, String recipient, String recipientNumber, String deliveryDescription, boolean isDefaultAddress) {
		this.user = user;
		this.address = address;
		this.zipCode = zipCode;
		this.recipient = recipient;
		this.recipientNumber = recipientNumber;
		this.deliveryDescription = deliveryDescription;
		this.isDefaultAddress = isDefaultAddress;
	}

	public void update(UpdateAddressRequestDto dto){
		if(dto.getZipCode() != null) this.zipCode = dto.getZipCode();
		if(dto.getAddress() != null) this.address = dto.getAddress();
		if(dto.getRecipient() != null) this.recipient = dto.getRecipient();
		if(dto.getRecipientNumber() != null) this.recipientNumber = dto.getRecipientNumber();
		if(dto.getDeliveryDescription() != null) this.deliveryDescription = dto.getDeliveryDescription();
		if(dto.getIsDefaultAddress() != null) this.isDefaultAddress = dto.getIsDefaultAddress();
	}

	/**
	 * LockTest용 생성자
	 */

	public Address(User user, Long zipCode, String address, String recipient, String recipientNumber, String deliveryDescription, Boolean isDefaultAddress) {
		this.user = user;
		this.zipCode = zipCode;
		this.address = address;
		this.recipient = recipient;
		this.recipientNumber = recipientNumber;
		this.deliveryDescription = deliveryDescription;
		this.isDefaultAddress = isDefaultAddress;
	}
}
