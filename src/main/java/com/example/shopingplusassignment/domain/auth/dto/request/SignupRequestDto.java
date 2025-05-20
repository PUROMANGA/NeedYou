package com.example.shopingplusassignment.domain.auth.dto.request;

import com.example.shopingplusassignment.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    private final String name;
    private final String password;
    private final String email;
    private final String phone;
    private final String UserRole;


    public SignupRequestDto(String name, String password, String email, String phone, String userRole) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        UserRole = userRole;
    }
}
