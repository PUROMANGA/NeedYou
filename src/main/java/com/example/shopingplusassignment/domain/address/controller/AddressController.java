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

	@PostMapping
	public ResponseEntity<SaveAddressResponseDto> saveAddress (
		@RequestBody SaveAddressRequestDto requestDto,
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.save(requestDto,userId), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<AddressResponseDto>> findAllAddress (
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.findAll(userId), HttpStatus.OK);
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<DetailAddressResponseDto> findOneAddress (
		@PathVariable Long addressId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.findOne(addressId,userId), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<DetailAddressResponseDto> findDefaultAddress (
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.findDefaultOne(userId), HttpStatus.OK);
	}

	@PatchMapping("/{addressId}")
	public ResponseEntity<DetailAddressResponseDto> updateAddress (
		@PathVariable Long addressId,
		@RequestBody UpdateAddressRequestDto resquestDto,
		@AuthenticationPrincipal AuthUser authUser
	) {
		Long userId = authUser.getUser().getId();
		return new ResponseEntity<>(addressService.update(addressId,resquestDto, userId), HttpStatus.OK);
	}

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
