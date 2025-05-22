package com.example.shopingplusassignment.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private final String name;
    private final String phone;
    private final String password;

    public UserUpdateRequestDto(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }
}
