package com.example.shopingplusassignment.domain.auth.controller;

import com.example.shopingplusassignment.domain.auth.dto.request.LoginRequestDto;
import com.example.shopingplusassignment.domain.auth.dto.request.SignupRequestDto;
import com.example.shopingplusassignment.domain.auth.dto.response.LoginResponseDto;
import com.example.shopingplusassignment.domain.auth.dto.response.SignupResponseDto;
import com.example.shopingplusassignment.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }
}
