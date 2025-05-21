package com.example.shopingplusassignment.domain.cart.repository;

import com.example.shopingplusassignment.domain.cart.dto.CartProductDto;
import com.example.shopingplusassignment.domain.cart.dto.CartResponseDto;
import com.example.shopingplusassignment.domain.cart.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserId(Long userId);

    @Query("select new com.example.shopingplusassignment.domain.cart.dto.CartProductDto(" +
            "a, c.cartId, p.name, p.price, sum(c.amount * p.price)) " +
            "from Cart c " +
            "join Product p on p.id = c.productId " +
            "join Address a on a.user.id = c.userId " +
            "where c.userId = :userId " +
            "group by a, c.cartId, p.name, p.price")
    List<CartProductDto> findCartAndProductName(Long userId);

    Page<CartResponseDto> findCartResponseDtoByUserId(Long userId, Pageable pageable);
}
