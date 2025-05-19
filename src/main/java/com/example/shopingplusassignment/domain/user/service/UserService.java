package com.example.shopingplusassignment.domain.user.service;

import com.example.shopingplusassignment.domain.user.dto.request.UserUpdateRequest;
import com.example.shopingplusassignment.domain.user.dto.response.UserResponse;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 조회
     * @param userId
     * @return
     */
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + userId)
        );
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone());
    }

}
