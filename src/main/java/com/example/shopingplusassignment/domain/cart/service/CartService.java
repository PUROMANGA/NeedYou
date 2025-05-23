package com.example.shopingplusassignment.domain.cart.service;

import com.example.shopingplusassignment.domain.cart.dto.CartRequestDto;
import com.example.shopingplusassignment.domain.cart.dto.CartResponseDto;
import com.example.shopingplusassignment.domain.cart.entity.Cart;
import com.example.shopingplusassignment.domain.cart.repository.CartRepository;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import com.example.shopingplusassignment.error.CustomRuntimeException;
import com.example.shopingplusassignment.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    /**
     * requestDto로 amount, productId와 userDetail에서 각각의 정보를 받아 db에 저장합니다.
     * @param cartRequestDto
     * @param productId
     * @param email
     * @return
     */
    @Transactional
    public CartResponseDto postCartService(CartRequestDto cartRequestDto, Long productId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.PRODUCT_CANT_FIND));
        Cart cart = cartRepository.save(new Cart(productId, user.getId(), cartRequestDto));
        return new CartResponseDto(cart, product);
    }

    /**
     * cartId를 찾아서, 그 카트 안에 있는 수량을 바꿔서 db에 저장하게 합니다.
     * @param cartId
     * @param cartRequestDto
     * @return
     */
    @Transactional
    public Cart patchCartService(Long cartId, CartRequestDto cartRequestDto) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("카트가 없습니다."));
        cart.update(cartRequestDto);
        return cartRepository.save(cart);
    }

    /**
     * 유저의 정보만 받아서, 유저의 정보로 카트를 조회한다음, 그 카트를 페이징해서 보여줍니다.
     * @param userId
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<CartResponseDto> getCartService(Long userId, Pageable pageable) {
        Page<CartResponseDto> cart = cartRepository.findCartResponseDtoByUserId(userId, pageable);
        if(cart.isEmpty()) {
            throw new RuntimeException("카트가 비어있습니다");
        }
        return cart;
    }

    /**
     * 유저가 가지고 있는 여러가지 카트 중 하나를 골라서 삭제합니다.
     * @param cartId
     * @param email
     */
    @Transactional
    public void deleteCartService(Long cartId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("카트가 없습니다."));
        if(!cart.getUserId().equals(user.getId())) {
            throw new RuntimeException("등록자와 일치하지 않습니다");
        }
        cartRepository.delete(cart);
    }
}
