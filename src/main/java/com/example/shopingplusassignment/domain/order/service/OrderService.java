package com.example.shopingplusassignment.domain.order.service;

import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.address.repository.AddressRepository;
import com.example.shopingplusassignment.domain.cart.entity.Cart;
import com.example.shopingplusassignment.domain.cart.repository.CartRepository;
import com.example.shopingplusassignment.domain.order.dto.ResponseOrderDto;
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
import error.CustomRuntimeException;
import error.ExceptionCode;
import lombok.RequiredArgsConstructor;
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
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final ProductOrderRepository productOrderRepository;


    /**
     * email로 user의 id를 찾아서 필요한 정보를 꺼내옵니다.
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public List<ResponseOrderDto> getOrderListService(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));
        return orderRepository.findOrdersByEmail(user.getId());
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
        Order savedOrder = orderRepository.save(order);
        List<ResponseProductOrderDto> responseProductOrderDtoList = productOrderRepository.responseProductOrderDto(savedOrder.getId());
        List<ProductOrder> productOrderList = responseProductOrderDtoList
                .stream()
                .map(ResponseProductOrderDto::toEntity)
                .toList();
        productOrderRepository.saveAll(productOrderList);
        Cart cart = cartRepository.findById(user.getId()).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.VALID_EXCEPTION));
        cartRepository.delete(cart);
        return new ResponseSavedOrderDto(savedOrder);
    }

    @Transactional
    public Page<ResponseSavedOrderListDto> getOrderListByIdService(Long orderId, Pageable pageable) {
        Page<ResponseSavedOrderListDto> responseSavedOrderListDtos = productOrderRepository.findByOrderId(orderId, pageable);
        return responseSavedOrderListDtos;
    }
}
