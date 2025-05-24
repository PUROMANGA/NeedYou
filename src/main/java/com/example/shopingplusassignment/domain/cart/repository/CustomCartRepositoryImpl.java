package com.example.shopingplusassignment.domain.cart.repository;

import com.example.shopingplusassignment.domain.cart.dto.CartResponseOrderDto;
import com.example.shopingplusassignment.domain.cart.entity.QCart;
import com.example.shopingplusassignment.domain.order.entity.QOrder;
import com.example.shopingplusassignment.domain.product.entity.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor

public class CustomCartRepositoryImpl implements CustomCartRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QOrder order = QOrder.order;
    QCart cart = QCart.cart;
    QProduct product = QProduct.product;

    @Override
    public List<CartResponseOrderDto> findResponseCartOrderDtoByOrderId(Long orderId) {

        List<CartResponseOrderDto> result = jpaQueryFactory
                .select(Projections.constructor(CartResponseOrderDto.class,
                        product.id,
                        product.name,
                        product.price,
                        cart.amount,
                        cart.amount.multiply(product.price),
                        order,
                        order.creatTime,
                        order.modifiedTime))
                .from(cart)
                .join(product).on(cart.productId.eq(product.id))
                .join(order).on(cart.userId.eq(order.user.id))
                .where(order.id.eq(orderId))
                .fetch();

        if(result == null) {
            throw new RuntimeException("오류 메세지");
        }

        return result;
    }
}
