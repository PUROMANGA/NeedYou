package com.example.shopingplusassignment.domain.address.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table (name = "address")
public class AddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column (nullable = false)
	private Long zipCode;

	@Column (nullable = false)
	private String address;

	@Column (nullable = false)
	private String recipient;

	@Column (nullable = false)
	private Long recipientNumber;

	private String deliveryDescription;

	@Column (nullable = false)
	private boolean isDefaultAddress;



}
