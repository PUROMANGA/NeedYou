package com.example.shopingplusassignment.domain.seller.repository;

import com.example.shopingplusassignment.domain.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
