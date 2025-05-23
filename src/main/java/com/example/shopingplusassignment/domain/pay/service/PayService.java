package com.example.shopingplusassignment.domain.pay.service;

import com.example.shopingplusassignment.domain.pay.dto.PayRequestDto;
import com.example.shopingplusassignment.domain.pay.dto.PayResponseDto;
import com.example.shopingplusassignment.domain.pay.entity.Pay;
import com.example.shopingplusassignment.domain.pay.repository.PayRepository;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import com.example.shopingplusassignment.global.Event.OrderChangeStatusEvent;
import com.example.shopingplusassignment.error.CustomRuntimeException;
import com.example.shopingplusassignment.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.shopingplusassignment.domain.user.entity.User;

@Service
@RequiredArgsConstructor

public class PayService {

    private final PayRepository payRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    public PayResponseDto postPayService(Long orderId, String email, PayRequestDto payRequestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomRuntimeException(ExceptionCode.USER_CANT_FIND));
        Pay pay = new Pay(payRequestDto, user);
        Pay savedPay = payRepository.save(pay);
        publisher.publishEvent(new OrderChangeStatusEvent(this, orderId));
        return new PayResponseDto(savedPay);
    }
}
