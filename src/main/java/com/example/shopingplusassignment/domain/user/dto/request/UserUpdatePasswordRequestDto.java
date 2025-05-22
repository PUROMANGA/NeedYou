package com.example.shopingplusassignment.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserUpdatePasswordRequestDto {

    private final String oldPassword;
    private final String newPassword;

    public UserUpdatePasswordRequestDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
