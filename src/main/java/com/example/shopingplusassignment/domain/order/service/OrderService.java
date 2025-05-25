package com.example.shopingplusassignment.domain.order.service;

import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.address.repository.AddressRepository;
import com.example.shopingplusassignment.domain.cart.dto.CartProductDto;
import com.example.shopingplusassignment.domain.cart.dto.CartResponseOrderDto;
import com.example.shopingplusassignment.domain.cart.repository.CartRepository;
import com.example.shopingplusassignment.domain.config.RedissonLock;
import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderDto;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderListDto;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.order.repository.OrderRepository;
import com.example.shopingplusassignment.domain.product.dto.ProductStockDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import com.example.shopingplusassignment.domain.productOrder.repository.ProductOrderRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import com.example.shopingplusassignment.global.Event.CartClearEvent;
import com.example.shopingplusassignment.error.CustomRuntimeException;
import com.example.shopingplusassignment.error.ExceptionCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
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
    private final RedissonClient redissonClient;
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
        //email로 유저 찾아줌
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));

        //유저가 설정한 기본 배송지 찾아줌
        Address addresses = addressRepository.findDefaultAddress(user.getId());

        //오더 객체 만들어주고 해당 오더의 상태를 `준비중`로 설정
        Order order = new Order(user, addresses.getId());
        order.changeOrderStatus(OrderStatus.PENDING);

        //오더를 저장
        Order savedOrder = orderRepository.save(order);

        //오더의 id를 가지고 유저를 찾아주고, 유저가 담은 카트를 전부 ProdutOrder로 바꾸어 저장을 합니다.
        List<CartResponseOrderDto> responseProductOrderDtoList = cartRepository.findResponseCartOrderDtoByOrderId(savedOrder.getId());

        List<ProductOrder> productOrderList = responseProductOrderDtoList
                .stream()
                .map(CartResponseOrderDto::toEntity)
                .toList();

        productOrderRepository.saveAll(productOrderList);

        //그리고 아까 저장한 order를 다시 불러와서 다시 한 번 저장을 합니다.
        Order finalOrder = new Order(order, productOrderList);
        Order finalSavedOrder = orderRepository.save(finalOrder);

        //그리고 스프링 이벤트를 사용해 Cart를 비워줍니다.
        publisher.publishEvent(new CartClearEvent(this, user.getId()));

        //저는 이 시점에서 Lock을 걸어줄건데요.

        for(ProductOrder productOrder : productOrderList) {
            Long productId = productOrder.getProductId();
            Product product = productRepository.findProductById(productId);
            product.decreaseStock(productOrder.getAmount());
            productRepository.save(product);
        }

        //클라이언트에게는 어떤 오더를 했는지 보여줘야하기 때문에 최종적인 Order를 return해줍니다.
        return new ResponseSavedOrderDto(finalSavedOrder);
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


    /**
     * 낙관적 락 로직
     */

    @Transactional
    public ResponseSavedOrderDto postOrderServiceOptimisitcLockTestMethod(String email) {
        //email로 유저 찾아줌
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));

        //유저가 설정한 기본 배송지 찾아줌
        Address addresses = addressRepository.findDefaultAddress(user.getId());

        //오더 객체 만들어주고 해당 오더의 상태를 `준비중`로 설정
        Order order = new Order(user, addresses.getId());
        order.changeOrderStatus(OrderStatus.PENDING);

        //오더를 저장
        Order savedOrder = orderRepository.save(order);

        //오더의 id를 가지고 유저를 찾아주고, 유저가 담은 카트를 전부 ProdutOrder로 바꾸어 저장을 합니다.
        List<CartResponseOrderDto> responseProductOrderDtoList = cartRepository.findResponseCartOrderDtoByOrderId(savedOrder.getId());

        List<ProductOrder> productOrderList = responseProductOrderDtoList
                .stream()
                .map(CartResponseOrderDto::toEntity)
                .toList();

        productOrderRepository.saveAll(productOrderList);

        //그리고 아까 저장한 order를 다시 불러와서 다시 한 번 저장을 합니다.
        Order finalOrder = new Order(order, productOrderList);
        Order finalSavedOrder = orderRepository.save(finalOrder);

        //그리고 스프링 이벤트를 사용해 Cart를 비워줍니다.
        publisher.publishEvent(new CartClearEvent(this, user.getId()));

        //저는 이 시점에서 Lock을 걸어줄건데요.

        for(ProductOrder productOrder : productOrderList) {
            Long productId = productOrder.getProductId();
            Product product = productRepository.findProductById(productId);
            product.decreaseStock(productOrder.getAmount());
            productRepository.save(product);
        }

        //클라이언트에게는 어떤 오더를 했는지 보여줘야하기 때문에 최종적인 Order를 return해줍니다.
        return new ResponseSavedOrderDto(finalSavedOrder);
    }

    /**
     * 분산락 로직
     */

    @Transactional
    public ResponseSavedOrderDto postOrderServiceDistributedLockTestMethod(String email) throws InterruptedException {
        //email로 유저 찾아줌
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));

        //유저가 설정한 기본 배송지 찾아줌
        Address addresses = addressRepository.findDefaultAddress(user.getId());

        //오더 객체 만들어주고 해당 오더의 상태를 `준비중`로 설정
        Order order = new Order(user, addresses.getId());
        order.changeOrderStatus(OrderStatus.PENDING);

        //오더를 저장
        Order savedOrder = orderRepository.save(order);

        //오더의 id를 가지고 유저를 찾아주고, 유저가 담은 카트를 전부 ProdutOrder로 바꾸어 저장을 합니다.
        List<CartResponseOrderDto> responseProductOrderDtoList = cartRepository.findResponseCartOrderDtoByOrderId(savedOrder.getId());

        List<ProductOrder> productOrderList = responseProductOrderDtoList
                .stream()
                .map(CartResponseOrderDto::toEntity)
                .toList();

        productOrderRepository.saveAll(productOrderList);

        //그리고 아까 저장한 order를 다시 불러와서 다시 한 번 저장을 합니다.
        Order finalOrder = new Order(order, productOrderList);
        Order finalSavedOrder = orderRepository.save(finalOrder);

        //그리고 스프링 이벤트를 사용해 Cart를 비워줍니다.
        publisher.publishEvent(new CartClearEvent(this, user.getId()));

        //저는 이 시점에서 Lock을 걸어줄건데요.

        for(ProductOrder productOrder : productOrderList) {
            Long productId = productOrder.getProductId();
            boolean acquired = false;
            RLock lock = redissonClient.getLock("lock:product:" + productId);

            while (true) {
                try {
                    boolean locked = lock.tryLock(5000, 3000, TimeUnit.MILLISECONDS);
                    if (locked) break;
                    Thread.sleep(50);

                    Product product = productRepository.findProductById(productId);
                    product.decreaseStock(productOrder.getAmount());
                    productRepository.save(product);

                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock(); // 내가 잡은 락이면 해제
                    }
                }
            }

        }

        //클라이언트에게는 어떤 오더를 했는지 보여줘야하기 때문에 최종적인 Order를 return해줍니다.
        return new ResponseSavedOrderDto(finalSavedOrder);
    }
}
