package com.example.shopingplusassignment.domain.seller.repository;

import com.example.shopingplusassignment.domain.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByUserId(Long userId);

    Seller findByEmail(String email);
}
