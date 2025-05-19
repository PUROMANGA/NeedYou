package com.example.shopingplusassignment.domain.address.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import com.example.shopingplusassignment.domain.address.service.AddressServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

	private final AddressServiceImpl addressService;

	@PostMapping
	public ResponseEntity<DetailAddressResponseDto> saveAddress (
		@RequestBody SaveAddressRequestDto requestDto

	) {
		return new ResponseEntity<>(addressService.save(requestDto,userId));
	}

	@GetMapping
	public List<ResponseEntity<AddressResponseDto>> findAllAddress (

	) {
		return new ResponseEntity<>(addressService.findAll(userId));
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<DetailAddressResponseDto> findOneAddress (
		@PathVariable Long addressId

	) {
		return new ResponseEntity<>(addressService.findOne(addressId,userId));
	}

	@PatchMapping("/{addressId}")
	public ResponseEntity<DetailAddressResponseDto> updateAddress (
		@PathVariable Long addressId,
		@RequestBody UpdateAddressRequestDto resquestDto

	) {
		return new ResponseEntity<>(addressService.update(addressId,resquestDto, userId));
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<String> deleteAddress (
		@PathVariable Long addressId

	) {
		addressService.delete(addressId, userId);
		return ResponseEntity.ok("배송정보가 삭제되었습니다");
	}

}
