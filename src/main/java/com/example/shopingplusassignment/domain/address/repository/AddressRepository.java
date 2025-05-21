package com.example.shopingplusassignment.domain.address.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.user.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long> {

	Optional<Address> findByIdAndUser_Id(Long addressId, Long userId);

	List<Address> findAllByUser_Id(Long userId);

	long countByUser_Id(Long userId);

	@Query("SELECT COUNT(a) > 0 FROM Address a WHERE a.user.id = :userId AND a.isDefaultAddress = true")
	boolean existsDefaultAddress(Long userId);

	@Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isDefaultAddress = true")
	Optional<Address> findDefaultAddress (Long userId);

}