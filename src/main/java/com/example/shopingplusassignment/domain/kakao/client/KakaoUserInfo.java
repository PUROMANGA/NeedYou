package com.example.shopingplusassignment.domain.kakao.client;

import com.example.shopingplusassignment.domain.user.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoUserInfo {
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone_number")
    private String phone;
    private UserRole userRole = UserRole.USER;
}
