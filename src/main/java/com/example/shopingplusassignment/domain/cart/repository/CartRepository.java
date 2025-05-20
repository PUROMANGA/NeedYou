package com.example.shopingplusassignment.domain.cart.repository;

import com.example.shopingplusassignment.domain.cart.dto.CartResponseDto;
import com.example.shopingplusassignment.domain.cart.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Page<CartResponseDto> findByIdForPage(Long userId, Pageable pageable);

    List<Cart> findAllByUserId(Long userId);
}
