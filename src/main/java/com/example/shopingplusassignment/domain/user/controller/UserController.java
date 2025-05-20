package com.example.shopingplusassignment.domain.user.controller;

import com.example.shopingplusassignment.domain.user.dto.response.UserResponse;
import com.example.shopingplusassignment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

}
