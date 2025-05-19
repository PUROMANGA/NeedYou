package com.example.shopingplusassignment.domain.address.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shopingplusassignment.domain.address.dto.Request.SaveAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Request.UpdateAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Response.AddressResponseDto;
import com.example.shopingplusassignment.domain.address.dto.Response.DetailAddressResponseDto;

@Service
public interface AddressService {
	DetailAddressResponseDto save (SaveAddressRequestDto requestDto ,Long userId);

	List<AddressResponseDto> findAll (Long userId);

	DetailAddressResponseDto findOne(Long addressId, Long userId);

	DetailAddressResponseDto update(Long addressId, UpdateAddressRequestDto requestDto, Long userId);

	void delete(Long addressId, Long userId);
}
