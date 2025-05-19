package com.example.shopingplusassignment.domain.address.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shopingplusassignment.domain.address.dto.Request.SaveAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Request.UpdateAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Response.FindAddressResponseDto;
import com.example.shopingplusassignment.domain.address.dto.Response.SaveAddressResponseDto;

@Service
public interface AddressService {
	SaveAddressResponseDto save (SaveAddressRequestDto requestDto ,Long userId);

	List<SaveAddressResponseDto> findAll (Long userId);

	FindAddressResponseDto findOne(Long addressId, Long userId);

	FindAddressResponseDto update(Long addressId, UpdateAddressRequestDto requestDto, Long userId);

	void delete();
}
