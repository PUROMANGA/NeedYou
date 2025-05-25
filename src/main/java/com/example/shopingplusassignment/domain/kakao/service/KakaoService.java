package com.example.shopingplusassignment.domain.kakao.service;

import com.example.shopingplusassignment.domain.config.JwtUtil;
import com.example.shopingplusassignment.domain.kakao.client.KakaoClient;
import com.example.shopingplusassignment.domain.kakao.client.KakaoUserInfo;
import com.example.shopingplusassignment.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoClient kakaoClient;
    private final JwtUtil jwtUtil;

    //    1. 로그인 페이지 uri 생성
    public String generateLoginPageUrl() {
        return kakaoClient.generateLoginPageUrl();
    }

    public String kakaoLogin(String authorizationCode) {
        //    1. authorizationCode 로 해당 provider 의 accessToken1 조회
        String accessToken = kakaoClient.getAccessToken(authorizationCode);
        //    2. accessToken1로 사용자의 대한 정보 조회
        KakaoUserInfo kakaoUserInfo = kakaoClient.kakaoUserInfo(accessToken);
        //    3. (만약, 처음이라면 회원가입 처리) member, social_member
        User user = kakaoClient.registerIfAbsent(kakaoUserInfo);
        //    4. 인증에 성공했기 때문에 accessToken2 만들어서 반환
        return jwtUtil.createAccessToken(user);
    }
}
