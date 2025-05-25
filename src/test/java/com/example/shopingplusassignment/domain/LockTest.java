package com.example.shopingplusassignment.domain;

import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.address.repository.AddressRepository;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.cart.entity.Cart;
import com.example.shopingplusassignment.domain.cart.repository.CartRepository;
import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.order.repository.OrderRepository;
import com.example.shopingplusassignment.domain.order.service.OrderService;
import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import com.example.shopingplusassignment.domain.productOrder.repository.ProductOrderRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")

public class LockTest {

    private static final Logger log = LoggerFactory.getLogger(LockTest.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    @DisplayName("비관적 락 테스트")
    public void pessimisticLockTest() throws InterruptedException {

        //given
        String email = "test@email.com";
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        User user = new User(
                "김땡중",
                "testpw123",
                "test@email.com",
                "0101234564",
                UserRole.USER,
                false
        );

        userRepository.save(user);

        Address address = new Address(
                user,
                123L,
                "address",
                "recipient",
                "recipientNumber",
                "deliveryDescription",
                true
        );

        addressRepository.save(address);

        Seller seller = new Seller(
                "스파르타",
                "김호중",
                "seller@email.com",
                "1234",
                "우리집",
                "5678",
                user
        );

        sellerRepository.save(seller);

        Brand brand = new Brand(
                "삼성",
                seller
        );

        brandRepository.save(brand);

        Product product = productRepository.save(
                new Product("엄청난 상품", "설명", 5000000L, 1L, ProductCategory.COOLER, brand, seller.getId())
        );

        Cart cart = new Cart(
                product.getId(),
                user.getId(),
                1L
        );

        cartRepository.save(cart);

        //when
        for(int i = 0; i < threadCount; i++) {
            String threadName = "Thread" + i;
            Thread.currentThread().setName(threadName);
            executor.submit(() -> {
                try {
                    log.info("{} : 주문시도", threadName);
                    orderService.postOrderService(email);
                    log.info("{} : 주문성공", threadName);
                } catch (Exception e) {
                    log.error("{} : 주문실패 - {}", threadName, e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product result = productRepository.findById(product.getId()).orElseThrow();

        //then
        assertThat(result.getStock()).isEqualTo(0L);
    }

    @Test
    @DisplayName("낙관적 락 테스트")
    public void optimisticLockTest() throws InterruptedException {

        //given
        String email = "test@email.com";
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        User user = new User(
                "김땡중",
                "testpw123",
                "test@email.com",
                "0101234564",
                UserRole.USER,
                false
        );

        userRepository.save(user);

        Address address = new Address(
                user,
                123L,
                "address",
                "recipient",
                "recipientNumber",
                "deliveryDescription",
                true
        );

        addressRepository.save(address);

        Seller seller = new Seller(
                "스파르타",
                "김호중",
                "seller@email.com",
                "1234",
                "우리집",
                "5678",
                user
        );

        sellerRepository.save(seller);

        Brand brand = new Brand(
                "삼성",
                seller
        );

        brandRepository.save(brand);

        Product product = productRepository.save(
                new Product("엄청난 상품", "설명", 5000000L, 1L, ProductCategory.COOLER, brand, seller.getId())
        );

        Cart cart = new Cart(
                product.getId(),
                user.getId(),
                1L
        );

        cartRepository.save(cart);

        //when
        for(int i = 0; i < threadCount; i++) {
            String threadName = "Thread" + i;
            Thread.currentThread().setName(threadName);
            executor.submit(() -> {
                try {
                    log.info("{} : 주문시도", threadName);
                    orderService.postOrderServiceOptimisitcLockTestMethod(email);
                    log.info("{} : 주문성공", threadName);
                } catch (Exception e) {
                    log.error("{} : 주문실패 - {}", threadName, e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product result = productRepository.findById(product.getId()).orElseThrow();

        //then
        assertThat(result.getStock()).isEqualTo(0L);
    }

    @Test
    @DisplayName("분산 락 테스트")
    public void distributedLockTest() throws InterruptedException {

        //given
        String email = "test@email.com";
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        User user = new User(
                "김땡중",
                "testpw123",
                "test@email.com",
                "0101234564",
                UserRole.USER,
                false
        );

        userRepository.save(user);

        Address address = new Address(
                user,
                123L,
                "address",
                "recipient",
                "recipientNumber",
                "deliveryDescription",
                true
        );

        addressRepository.save(address);

        Seller seller = new Seller(
                "스파르타",
                "김호중",
                "seller@email.com",
                "1234",
                "우리집",
                "5678",
                user
        );

        sellerRepository.save(seller);

        Brand brand = new Brand(
                "삼성",
                seller
        );

        brandRepository.save(brand);

        Product product = productRepository.save(
                new Product("엄청난 상품", "설명", 5000000L, 1L, ProductCategory.COOLER, brand, seller.getId())
        );

        Cart cart = new Cart(
                product.getId(),
                user.getId(),
                1L
        );

        cartRepository.save(cart);

        //when
        for(int i = 0; i < threadCount; i++) {
            String threadName = "Thread" + i;
            Thread.currentThread().setName(threadName);
            executor.submit(() -> {
                try {
                    log.info("{} : 주문시도", threadName);
                    orderService.postOrderServiceDistributedLockTestMethod(email);
                    log.info("{} : 주문성공", threadName);
                } catch (Exception e) {
                    log.error("{} : 주문실패 - {}", threadName, e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product result = productRepository.findById(product.getId()).orElseThrow();

        //then
        assertThat(result.getStock()).isEqualTo(0L);
    }
}
