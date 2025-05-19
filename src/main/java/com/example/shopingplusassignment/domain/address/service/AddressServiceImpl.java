package com.example.shopingplusassignment.domain.address.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopingplusassignment.domain.address.dto.Request.SaveAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Request.UpdateAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Response.AddressResponseDto;
import com.example.shopingplusassignment.domain.address.dto.Response.DetailAddressResponseDto;
import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.address.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;

	@Override
	public DetailAddressResponseDto save(SaveAddressRequestDto dto, Long userId) {

		Address address = new Address(
			user,
			dto.getAddress(),
			dto.getZipCode(),
			dto.getRecipient(),
			dto.getRecipientNumber(),
			dto.getDeliveryDescription(),
			dto.getIsDefaultAddress()
		);

		addressRepository.save(address);

		return DetailAddressResponseDto.toDto(address);
	}

	@Override
	public List<AddressResponseDto> findAll(Long userId) {

		List<Address> addressList = addressRepository.findAllByUser_Id(userId);

		return addressList.stream().map(address -> new AddressResponseDto(
			address.getId(),
			address.getAddress(),
			address.getIsDefaultAddress()
		)).toList();
	}

	@Override
	public DetailAddressResponseDto findOne(Long addressId, Long userId) {

		Address address = addressRepository.findByIdAndUser_Id(addressId, userId).orElseThrow(()->new RuntimeException(""));

		return DetailAddressResponseDto.toDto(address);
	}

	@Transactional
	@Override
	public DetailAddressResponseDto update(Long addressId, UpdateAddressRequestDto dto, Long userId) {

		Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
			.orElseThrow(()->new RuntimeException(""));

		List<Address> addressList = addressRepository.findAllByUser_Id(userId);

		if(dto.getIsDefaultAddress() != null && dto.getIsDefaultAddress()) {
			for (Address address1 : addressList) {
				address1.setIsDefaultAddress(false);
			}
		}

		address.update(dto);

		return DetailAddressResponseDto.toDto(address);
	}

	@Transactional
	@Override
	public void delete(Long addressId, Long userId) {

		Address address = addressRepository.findByIdAndUser_Id(addressId, userId).orElseThrow(()->new RuntimeException(""));

		addressRepository.delete(address);
	}
}
