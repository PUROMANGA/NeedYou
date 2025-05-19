package com.example.shopingplusassignment.domain.address.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shopingplusassignment.domain.address.dto.Request.SaveAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Request.UpdateAddressRequestDto;
import com.example.shopingplusassignment.domain.address.dto.Response.FindAddressResponseDto;
import com.example.shopingplusassignment.domain.address.dto.Response.SaveAddressResponseDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Override
	public SaveAddressResponseDto save(SaveAddressRequestDto requestDto, Long userId) {
		return null;
	}

	@Override
	public List<SaveAddressResponseDto> findAll(Long userId) {
		return List.of();
	}

	@Override
	public FindAddressResponseDto findOne(Long addressId, Long userId) {
		return null;
	}

	@Override
	public FindAddressResponseDto update(Long addressId, UpdateAddressRequestDto requestDto, Long userId) {
		return null;
	}

	@Override
	public void delete() {

	}
}
