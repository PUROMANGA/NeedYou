package com.example.shopingplusassignment.domain.cart.controller;


import com.example.shopingplusassignment.domain.cart.dto.CartRequestDto;
import com.example.shopingplusassignment.domain.cart.dto.CartResponseDto;
import com.example.shopingplusassignment.domain.cart.entity.Cart;
import com.example.shopingplusassignment.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")

public class CartController {

    private final CartService cartService;

    /**
     * requestDto로 amount, productId와 userDetail에서 각각의 정보를 받아 db에 저장합니다.
     * @param cartRequestDto
     * @param productId
     * @param userDeatil
     * @return
     */

    @PostMapping("products/{productId}")
    public ResponseEntity<CartResponseDto> postCartController(
            @RequestBody @Validated CartRequestDto cartRequestDto,
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDeatil userDeatil) {
        CartResponseDto cartResponseDto = cartService.postCartService(cartRequestDto, productId, userDeatil.getUsername());
        return ResponseEntity.ok(cartResponseDto);
    }

    /**
     * cartId를 찾아서, 그 카트 안에 있는 수량을 바꿔서 db에 저장하게 합니다.
     * @param cartId
     * @param cartRequestDto
     * @return
     */

    @PatchMapping("/{cartId}")
    public ResponseEntity<String> patchCartController(
            @PathVariable Long cartId,
            @RequestBody @Validated CartRequestDto cartRequestDto) {
        Cart cart = cartService.patchCartService(cartId, cartRequestDto);
        return ResponseEntity.ok("수량이" + cart.getAmount() + "개로 변경되었습니다.");
    }

    /**
     * 유저의 정보만 받아서, 유저의 정보로 카트를 조회한다음, 그 카트를 페이징해서 보여줍니다.
     * @param userDetail
     * @param pageable
     * @return
     */

    @GetMapping
    public ResponseEntity<Page<CartResponseDto>> getCartController(
            @AuthenticationPrincipal UserDetail userDetail,
            @PageableDefault(size = 10, sort = "creatTime", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(cartService.getCartService(userDetail.getUserId(), pageable));
    }

    /**
     * 유저가 가지고 있는 여러가지 카트 중 하나를 골라서 삭제합니다.
     * @param cartId
     * @param userDetail
     * @return
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCartController(
            @PathVariable Long cartId,
            @AuthenticationPrincipal UserDetail userDetail) {
        cartService.deleteCartService(cartId, userDetail.getUserName());
        return ResponseEntity.ok("장바구니 삭제가 완료되었습니다.");
    }
}
