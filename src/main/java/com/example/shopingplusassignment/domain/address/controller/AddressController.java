package com.example.shopingplusassignment.domain.address.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopingplusassignment.domain.address.dto.Request.SaveAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Request.UpdateAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Response.AddressResponseDto;
import com.example.shopingplusassignment.domain.address.dto.Response.DetailAddressResponseDto;
import com.example.shopingplusassignment.domain.address.dto.Response.SaveAddressResponseDto;
import com.example.shopingplusassignment.domain.address.service.AddressServiceImpl;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

	private final AddressServiceImpl addressService;

	/**
	 * 배송지 생성 요청 컨트롤러
	 *
	 * @param requestDto 배송지 생성 요청 정보가 담긴 {@link SaveAddressRequestDto} 객체
	 * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
	 * @return 생성된 배송지 정보가 담긴 {@link SaveAddressResponseDto} 객체
	 */
	@PostMapping
	public ResponseEntity<SaveAddressResponseDto> saveAddress (
		@RequestBody SaveAddressRequestDto requestDto,
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.save(requestDto,userId), HttpStatus.OK);
	}

	/**
	 * 배송지 전체 조회 요청 컨트롤러
	 *
	 * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
	 * @return 유저의 모든 배송지 정보가 담긴 {@link List<AddressResponseDto>} 객체
	 */
	@GetMapping
	public ResponseEntity<List<AddressResponseDto>> findAllAddress (
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.findAll(userId), HttpStatus.OK);
	}

	/**
	 * 배송지 단건 조회 요청 컨트롤러
	 *
	 * @param addressId 조회할 배송지의 ID
	 * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
	 * @return 유저의 배송지 정보가 담긴 {@link DetailAddressResponseDto} 객체
	 */
	@GetMapping("/{addressId}")
	public ResponseEntity<DetailAddressResponseDto> findOneAddress (
		@PathVariable Long addressId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.findOne(addressId,userId), HttpStatus.OK);
	}

	/**
	 * 기본 배송지 조회 요청 컨트롤러
	 *
	 * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
	 * @return 유저의 기본 배송지 정보가 담긴 {@link DetailAddressResponseDto} 객체
	 */
	@GetMapping("/default")
	public ResponseEntity<DetailAddressResponseDto> findDefaultAddress (
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.findDefaultOne(userId), HttpStatus.OK);
	}

	/**
	 * 배송지 수정 요청 컨트롤러
	 *
	 * @param addressId 조회할 배송지의 ID
	 * @param resquestDto 배송지 수정 요청 정보가 담긴 {@link UpdateAddressRequestDto} 객체
	 * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
	 * @return 수정된 배송지 정보가 담긴 {@link DetailAddressResponseDto} 객체
	 */
	@PatchMapping("/{addressId}")
	public ResponseEntity<DetailAddressResponseDto> updateAddress (
		@PathVariable Long addressId,
		@RequestBody UpdateAddressRequestDto resquestDto,
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.update(addressId,resquestDto, userId), HttpStatus.OK);
	}

	/**
	 * 배송지 삭제 요청 컨트롤러
	 *
	 * @param addressId 조회할 배송지의 ID
	 * @param authUser 로그인 토큰 정보가 담긴 {@link AuthUser} 객체
	 * @return 메세지 응답, 성공 - 200 / 실패 - 400
	 */
	@DeleteMapping("/{addressId}")
	public ResponseEntity<String> deleteAddress (
		@PathVariable Long addressId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		addressService.delete(addressId, userId);
		return ResponseEntity.ok("배송정보가 삭제되었습니다");
	}

}
