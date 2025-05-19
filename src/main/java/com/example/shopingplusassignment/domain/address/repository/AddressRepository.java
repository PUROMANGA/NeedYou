package com.example.shopingplusassignment.domain.address.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopingplusassignment.domain.address.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

	Optional<Address> findByIdAndUser_Id(Long addressId, Long userId);

	List<Address> findAllByUser_Id(Long userId);

}
