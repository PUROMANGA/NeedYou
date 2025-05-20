package com.example.shopingplusassignment.domain.order.repository;

import com.example.shopingplusassignment.domain.address.entity.QAddress;
import com.example.shopingplusassignment.domain.cart.entity.QCart;
import com.example.shopingplusassignment.domain.order.dto.ResponseOrderDto;
import com.example.shopingplusassignment.domain.order.entity.QOrder;
import com.example.shopingplusassignment.domain.product.entity.QProduct;
import com.example.shopingplusassignment.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor

public class CustomOrderRepositoryImpl implements CustomOrderRepository{

    private final JPAQueryFactory jpaQueryFactory;

    QOrder order = QOrder.order;
    QCart cart = QCart.cart;
    QProduct product = QProduct.product;
    QAddress address = QAddress.address1;
    QUser user = QUser.user;

    @Override
    public List<ResponseOrderDto> findOrdersByEmail(Long userid) {

        List<ResponseOrderDto> responseOrderDtos = jpaQueryFactory
                .select(Projections.constructor(ResponseOrderDto.class,
                        product.name,
                        product.price,
                        address,
                        cart,
                        cart.amount.multiply(product.price)))
                .from(order)
                .join(cart).on(cart.userId.eq(userid))
                .join(product).on(product.id.eq(cart.productId))
                .join(address).on(address.id.eq(order.addressId))
                .where(user.id.eq(userid))
                .fetch();

        if(responseOrderDtos == null) {
            throw new RuntimeException("OrderDtos not found");
        }

        return responseOrderDtos;
    }
}
