package com.example.shopingplusassignment.domain.pay.controller;

import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import com.example.shopingplusassignment.domain.pay.dto.PayRequestDto;
import com.example.shopingplusassignment.domain.pay.dto.PayResponseDto;
import com.example.shopingplusassignment.domain.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pays")
public class PayController {

    private final PayService payService;

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<PayResponseDto> postPayController(@PathVariable Long orderId,
                                                            @AuthenticationPrincipal AuthUser authUser,
                                                            @RequestBody @Validated PayRequestDto payRequestDto) {
        return ResponseEntity.ok(payService.postPayService(orderId, authUser.getUsername(), payRequestDto));
    }
}
