package com.example.shopingplusassignment.domain.seller.repository;

import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByUser(User user);

    Optional<Seller> findByUserId(Long userId);

}
