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

import error.CustomRuntimeException;
import error.ExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	/**
	 * 배송지 생성 요청 서비스
	 * 제약사항:
	 * 1. 사용자는 최대 10개의 배송지를 등록할 수 있습니다.
	 * 2. 첫 배송지 등록 시 기본 배송지가 없으면 자동으로 기본 배송지로 설정됩니다.
	 * 3. 기본 배송지가 이미 존재하면, 추가 배송지를 기본으로 설정할 수 없습니다.
	 *
	 * @param dto 배송지 생성 요청 정보가 담긴 {@link SaveAddressRequestDto} 객체
	 * @param userId 현재 로그인 유저 식별자
	 * @return 생성된 배송지 정보가 담긴 {@link SaveAddressResponseDto} 객체
	 */
	@Override
	public SaveAddressResponseDto save(SaveAddressRequestDto dto, Long userId) {

		// 로그인 유저 찾기
		User user = userRepository.findById(userId)
			.orElseThrow(()->new RuntimeException("사용자를 찾을 수 없습니다."));

		// 유저당 최대 10개까지 배송지 정보 등록 가능
		long countAddress = addressRepository.countByUser_Id(userId);
		if(countAddress >= 10) {
			throw new CustomRuntimeException(ExceptionCode.ADDRESS_LIMIT_OVER);
		}

		// 기본 배송지 1개 이상 설정 불가능
		if(dto.getIsDefaultAddress() != null && dto.getIsDefaultAddress()) {
			if (addressRepository.existsDefaultAddress(userId)) {
				throw new CustomRuntimeException(ExceptionCode.DEFAULT_ALREADY_EXISTS);
			}
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

	/**
	 * 배송지 전체 조회 요청 서비스
	 * 제약사항:
	 * 유저 본인의 배송지만 조회할 수 있습니다.
	 *
	 * @param userId 현재 로그인 유저 식별자
	 * @return 유저의 모든 배송지 정보가 담긴 {@link List<AddressResponseDto>} 객체
	 */
	@Override
	public List<AddressResponseDto> findAll(Long userId) {

		List<Address> addressList = addressRepository.findAllByUser_Id(userId);

		return addressList.stream().map(address -> new AddressResponseDto(
			address.getId(),
			address.getAddress(),
			address.getIsDefaultAddress()
		)).toList();
	}

	/**
	 * 배송지 단건 조회 요청 서비스
	 * 제약사항:
	 * 유저 본인의 배송지만 조회할 수 있습니다.
	 *
	 * @param addressId 배송지 정보 식별자
	 * @param userId 현재 로그인 유저 식별자
	 * @return 유저의 배송지 정보가 담긴 {@link DetailAddressResponseDto} 객체
	 */
	@Override
	public DetailAddressResponseDto findOne(Long addressId, Long userId) {

		Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
			.orElseThrow(()->new CustomRuntimeException(ExceptionCode.ADDRESS_NOT_FOUND));

		return DetailAddressResponseDto.toDto(address);
	}

	/**
	 * 기본 배송지 조회 요청 서비스
	 * 제약사항:
	 * 유저 본인의 배송지만 조회할 수 있습니다.
	 *
	 * @param userId 현재 로그인 유저 식별자
	 * @return 유저의 기본 배송지 정보가 담긴 {@link DetailAddressResponseDto} 객체
	 */
	@Override
	public DetailAddressResponseDto findDefaultOne(Long userId) {

		Address address = addressRepository.findDefaultAddress(userId);

		if(address == null) {
			throw new CustomRuntimeException(ExceptionCode.NO_DEFAULT_ADDRESS);
		}

		return DetailAddressResponseDto.toDto(address);
	}

	/**
	 * 배송지 수정 요청 서비스
	 * 제약사항:
	 * 1. 유저 본인의 배송지만 수정할 수 있습니다.
	 * 2. 기본 배송지로 수정할 경우 기존 기본 배송지는 해제됩니다.
	 *
	 * @param addressId 배송지 정보 식별자
	 * @param dto 배송지 수정 요청 정보가 담긴 {@link UpdateAddressRequestDto} 객체
	 * @param userId 현재 로그인 유저 식별자
	 * @return 수정된 배송지 정보가 담긴 {@link DetailAddressResponseDto} 객체
	 */
	@Transactional
	@Override
	public DetailAddressResponseDto update(Long addressId, UpdateAddressRequestDto dto, Long userId) {

		Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
			.orElseThrow(()->new CustomRuntimeException(ExceptionCode.ADDRESS_NOT_FOUND));

		// 기본 배송지 수정시 기존 기본 배송지 해제 처리
		if (dto.getIsDefaultAddress() != null && dto.getIsDefaultAddress()) {
			Address lastDefaultAddress = addressRepository.findDefaultAddress(userId);
			if (!lastDefaultAddress.equals(address)) {
				lastDefaultAddress.setIsDefaultAddress(false);
			}
		}

		address.update(dto);

		return DetailAddressResponseDto.toDto(address);
	}

	/**
	 * 배송지 삭제 요청 서비스
	 * 제약사항:
	 * 유저 본인의 배송지만 삭제할 수 있습니다.
	 *
	 * @param addressId 배송지 정보 식별자
	 * @param userId 현재 로그인 유저 식별자
	 */
	@Transactional
	@Override
	public void delete(Long addressId, Long userId) {

		Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
			.orElseThrow(()->new CustomRuntimeException(ExceptionCode.ADDRESS_NOT_FOUND));

		addressRepository.delete(address);
	}
}
