package com.example.shopingplusassignment.domain.seller.repository;

import com.example.shopingplusassignment.domain.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByUserId(Long userId);

    Seller findByEmail(String email);

    boolean existsByUserId(Long userId);

    @Query("SELECT s FROM Seller s JOIN FETCH s.user WHERE s.id = :id")
    Optional<Seller> findByIdFetchUser(@Param("id") Long id);

}
