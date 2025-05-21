package com.example.shopingplusassignment.domain.pay.entity;

import com.example.shopingplusassignment.domain.pay.dto.PayRequestDto;
import com.example.shopingplusassignment.domain.pay.dto.PayResponseDto;
import com.example.shopingplusassignment.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "pays")
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //카드라면 값이 들어가고, 현금이면 null
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Pay(PayRequestDto payRequestDto, User user) {
        this.name = payRequestDto.getName();
        this.paymentMethod = payRequestDto.getPaymentMethod();
        this.user = user;
    }
}
