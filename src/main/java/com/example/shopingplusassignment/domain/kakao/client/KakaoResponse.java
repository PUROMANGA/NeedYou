package com.example.shopingplusassignment.domain.kakao.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoResponse {
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoUserInfo kakaoUserInfo;
}
