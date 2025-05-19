package com.example.shopingplusassignment.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phone;

    public UserResponse(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
