package com.example.shopingplusassignment.global.Event;

import com.example.shopingplusassignment.domain.cart.entity.Cart;
import com.example.shopingplusassignment.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor

public class CartClearEventListener {

    private final CartRepository cartRepository;

    @Async
    @EventListener
    public void handleCartClearEvent(CartClearEvent cartClearEvent) {
        Long userId = cartClearEvent.getUserId();
        List<Cart> cart = cartRepository.findAllByUserId(userId);
        cartRepository.deleteAll(cart);
    }
}
