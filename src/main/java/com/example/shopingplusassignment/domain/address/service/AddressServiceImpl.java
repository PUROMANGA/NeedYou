package com.example.shopingplusassignment.domain.address.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopingplusassignment.domain.address.dto.Request.SaveAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Request.UpdateAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Response.AddressResponseDto;
import com.example.shopingplusassignment.domain.address.dto.Response.DetailAddressResponseDto;
import com.example.shopingplusassignment.domain.address.dto.Response.SaveAddressResponseDto;
import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.address.repository.AddressRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	@Override
	public SaveAddressResponseDto save(SaveAddressRequestDto dto, Long userId) {

		// 로그인 유저 찾기
		User user = userRepository.findById(userId)
			.orElseThrow(()->new RuntimeException(""));

		// 유저당 최대 10개까지 배송지 정보 등록 가능
		long countAddress = addressRepository.countByUser_Id(userId);
		if(countAddress >= 10) {
			throw new RuntimeException("");
		}

		// 기본 배송지 1개 이상 설정 불가능
		if(addressRepository.existsDefaultAddress(userId)) {
			throw new RuntimeException("");
		}

		// 기본 배송지 1개는 무조건 있어야함
		String message = "배송지 등록이 완료되었습니다.";
		if(!addressRepository.existsDefaultAddress(userId)) {
			if(!dto.getIsDefaultAddress()) {
				dto.setIsDefaultAddress(true);
				message = "기본 배송지가 없어 해당 주소를 기본 배송지로 설정합니다.";
			}
		}

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

		return new SaveAddressResponseDto(message, DetailAddressResponseDto.toDto(address));
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

		Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
			.orElseThrow(()->new RuntimeException(""));

		return DetailAddressResponseDto.toDto(address);
	}

	@Override
	public DetailAddressResponseDto findDefaultOne(Long userId) {

		Address address = addressRepository.findDefaultAddress(userId)
			.orElseThrow(()->new RuntimeException(""));

		return DetailAddressResponseDto.toDto(address);
	}

	@Transactional
	@Override
	public DetailAddressResponseDto update(Long addressId, UpdateAddressRequestDto dto, Long userId) {

		Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
			.orElseThrow(()->new RuntimeException(""));

		// 기본 배송지 수정시 기존 기본 배송지 해제 처리
		Address lastDefaultAddress = addressRepository.findDefaultAddress(userId)
				.orElseThrow(()-> new RuntimeException(""));
		lastDefaultAddress.setIsDefaultAddress(false);

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
