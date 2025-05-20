package com.example.shopingplusassignment.domain.order.controller;

import com.example.shopingplusassignment.domain.cart.dto.CartProductDto;
import com.example.shopingplusassignment.domain.cart.service.CartService;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import com.example.shopingplusassignment.domain.order.dto.ResponseOrderDto;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderDto;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderListDto;
import com.example.shopingplusassignment.domain.order.service.OrderService;
import com.example.shopingplusassignment.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")

public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    /**
     * email로 user의 id를 찾아서 주문 상품, 가격, 주소, 카트, 총 가격, 주문 상품 식별자 정보를 꺼내옵니다.
     * @param userDetail
     * @return
     */
    @GetMapping
    public ResponseEntity<List<CartProductDto>> getOrderListController(
            @AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok(orderService.getOrderListService(user.getUsername()));
    }

    /**
     * email로 user을 찾아주고, user의 id로 쿼리를 돌리다음, order의 table에 저장, cart는 price, amount를 이용해서 ProductOrder로 만든 후에 전부 삭제해줍니다.
     * @param userDetail
     * @return
     */

    @PostMapping
    public ResponseEntity<ResponseSavedOrderDto> postOrderController(
            @AuthenticationPrincipal AuthUser user) {
        return ResponseEntity.ok(orderService.postOrderService(user.getUsername()));
    }

    /**
     * orderId로 productOrder를 찾은다음, productOrder의 요소와 Order의 요소를 출력해준다.
     * @param orderId
     * @param pageable
     * @return
     */

    @GetMapping("/{orderId}")
    public ResponseEntity<Page<ResponseSavedOrderListDto>> getOrderListByIdController(
            @PathVariable Long orderId,
            @PageableDefault(size = 10, sort = "creatTime", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrderListByIdService(orderId, pageable));
    }
}
