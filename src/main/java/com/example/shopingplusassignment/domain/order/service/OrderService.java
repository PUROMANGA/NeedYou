package com.example.shopingplusassignment.domain.order.service;

import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.address.repository.AddressRepository;
import com.example.shopingplusassignment.domain.cart.dto.CartProductDto;
import com.example.shopingplusassignment.domain.cart.repository.CartRepository;
import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderDto;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderListDto;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.order.repository.OrderRepository;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.productOrder.dto.ResponseProductOrderDto;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import com.example.shopingplusassignment.domain.productOrder.repository.ProductOrderRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import com.example.shopingplusassignment.global.Event.CartClearEvent;
import error.CustomRuntimeException;
import error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final ProductOrderRepository productOrderRepository;
    private final ApplicationEventPublisher publisher;
    private final CartRepository cartRepository;

    /**
     * email로 user의 id를 찾아서 주문 상품, 가격, 주소, 카트, 총 가격, 주문 상품 식별자 정보를 꺼내옵니다.
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public List<CartProductDto> getOrderListService(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));
        Long userId = user.getId();
        List<CartProductDto> cartProductDtoList = cartRepository.findCartAndProductName(userId);
        return cartProductDtoList;
    }

    /**
     * email로 user을 찾아주고, user의 id로 쿼리를 돌리다음, order의 table에 저장, cart는 price, amount를 이용해서 ProductOrder로 만든 후에 전부 삭제해줍니다.
     * @param email
     * @return
     */

    @Transactional
    public ResponseSavedOrderDto postOrderService(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));
        Address addresses = addressRepository.findDefaultAddress(user.getId()).orElseThrow(() -> new RuntimeException("주소가 없음"));

        Order order = new Order(user, addresses.getId());
        order.changeOrderStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        List<ResponseProductOrderDto> responseProductOrderDtoList = productOrderRepository.findResponseProductOrderDtoByOrderId(savedOrder.getId());

        List<ProductOrder> productOrderList = responseProductOrderDtoList
                .stream()
                .map(ResponseProductOrderDto::toEntity)
                .toList();

        productOrderRepository.saveAll(productOrderList);

        publisher.publishEvent(new CartClearEvent(this, user.getId()));

        return new ResponseSavedOrderDto(savedOrder);
    }

    /**
     * orderId로 productOrder를 찾은다음, productOrder의 요소와 Order의 요소를 출력해준다.
     * @param orderId
     * @param pageable
     * @return
     */

    @Transactional(readOnly = true)
    public Page<ResponseSavedOrderListDto> getOrderListByIdService(Long orderId, Pageable pageable) {
        return productOrderRepository.findOrderPageByOrderId(orderId, pageable);
    }
}
