package com.example.shopingplusassignment.domain.kakao.controller;

import com.example.shopingplusassignment.domain.kakao.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login")
    public void redirectLoginPage(HttpServletResponse response) throws IOException {
        //    1. 사용자가 로그인 페이지로 Redirect 하는 API
        String loginPageUrl = kakaoService.generateLoginPageUrl();
        response.sendRedirect(loginPageUrl);
    }

    //    2. 사용자의 Authorization Code를 받아 최종 로그인까지 해주는 api
    @GetMapping("/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String authorizationCode){
        //        1. Authorization code 를 통해 OAuth2 Login 수행
        String accessToken = kakaoService.kakaoLogin(authorizationCode);
        //        2. 사용자에게 accessToken 같은 인증정보 반환
        return ResponseEntity.ok(accessToken);
    }


}
