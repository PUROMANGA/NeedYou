package com.example.shopingplusassignment.domain.productOrder.repository;


import com.example.shopingplusassignment.domain.cart.entity.QCart;
import com.example.shopingplusassignment.domain.order.dto.ResponseSavedOrderListDto;
import com.example.shopingplusassignment.domain.order.entity.QOrder;
import com.example.shopingplusassignment.domain.product.entity.QProduct;
import com.example.shopingplusassignment.domain.productOrder.dto.ResponseProductOrderDto;
import com.example.shopingplusassignment.domain.productOrder.entity.QProductOrder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor

public class CustomProductOrderRepositoryImpl implements CustomProductOrderRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QOrder order = QOrder.order;
    QCart cart = QCart.cart;
    QProduct product = QProduct.product;
    QProductOrder productOrder = QProductOrder.productOrder;

    @Override
    public List<ResponseProductOrderDto> findResponseProductOrderDtoByOrderId(Long orderId) {

        List<ResponseProductOrderDto> result = jpaQueryFactory
                .select(Projections.constructor(ResponseProductOrderDto.class,
                        productOrder.id,
                        product.name,
                        product.price,
                        cart.amount,
                        cart.amount.multiply(product.price),
                        order,
                        productOrder.creatTime,
                        productOrder.modifiedTime))
                .from(productOrder)
                .join(productOrder.order, order)
                .join(cart).on(order.user.id.eq(cart.userId))
                .join(product).on(cart.productId.eq(product.id))
                .fetch();

        if(result == null) {
            throw new RuntimeException("오류 메세지");
        }

        return result;
    }

    @Override
    public Page<ResponseSavedOrderListDto> findOrderPageByOrderId(Long orderId, Pageable pageable) {

        List<ResponseSavedOrderListDto> result = jpaQueryFactory
                .select(Projections.constructor(ResponseSavedOrderListDto.class,
                        order,
                        order.creatTime,
                        order.modifiedTime))
                .from(order)
                .where(order.id.eq(orderId))
                .fetch();

        long total = jpaQueryFactory
                .select(order.count())
                .from(order)
                .where(order.id.eq(orderId))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }
}
