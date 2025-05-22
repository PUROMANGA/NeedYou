package com.example.shopingplusassignment.domain.user.controller;

import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import com.example.shopingplusassignment.domain.user.dto.request.UserUpdatePasswordRequestDto;
import com.example.shopingplusassignment.domain.user.dto.request.UserUpdateRequestDto;
import com.example.shopingplusassignment.domain.user.dto.response.UserResponse;
import com.example.shopingplusassignment.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 유저 조회
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    /**
     * 유저 정보 변경
     * @param authUser
     * @param requestDto
     * @return
     */
    @PutMapping
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserUpdateRequestDto requestDto
    ){
        return ResponseEntity.ok(userService.updateUser(authUser.getUser(), requestDto));
    }

    /**
     * 비밀번호 변경
     * @param authUser
     * @param requestDto
     * @return
     */
    @PatchMapping
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserUpdatePasswordRequestDto requestDto
    ){
        userService.updatePassword(authUser.getUser(), requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal AuthUser authUser, HttpServletRequest request) {

        userService.logout(authUser, request);
        return ResponseEntity.noContent().build();
    }
}
