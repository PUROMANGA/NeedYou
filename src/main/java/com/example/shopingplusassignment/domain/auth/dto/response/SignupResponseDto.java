package com.example.shopingplusassignment.domain.auth.dto.response;

import com.example.shopingplusassignment.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {

    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final UserRole userRole;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final String bearerToken;

    public SignupResponseDto(Long id, String name, String email, String phone, UserRole userRole, LocalDateTime createdAt, LocalDateTime modifiedAt, String bearerToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.bearerToken = bearerToken;
    }
}
