package com.example.shopingplusassignment.domain.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopingplusassignment.domain.address.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
