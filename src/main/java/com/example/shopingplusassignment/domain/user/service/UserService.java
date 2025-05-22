package com.example.shopingplusassignment.domain.user.service;

import com.example.shopingplusassignment.domain.auth.repository.RefreshTokenRepository;
import com.example.shopingplusassignment.domain.common.dto.AuthUser;
import com.example.shopingplusassignment.domain.config.JwtUtil;
import com.example.shopingplusassignment.domain.user.dto.request.UserUpdatePasswordRequestDto;
import com.example.shopingplusassignment.domain.user.dto.request.UserUpdateRequestDto;
import com.example.shopingplusassignment.domain.user.dto.response.UserResponse;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

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

    /**
     *
     * @param user
     * @param requestDto
     * @return
     */
    @Transactional
    public UserResponse updateUser(User user, UserUpdateRequestDto requestDto) {

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 올바르지 않습니다.");
        }

        user.userUpdate(requestDto.getName(), requestDto.getPhone());

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone());
    }

    @Transactional
    public void updatePassword(User user, UserUpdatePasswordRequestDto requestDto) {
        if(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "새 비밀번호가 이전 비밀번호와 같을 수 없습니다.");
        }

        if(!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        String encodePassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(encodePassword);
    }

    public void logout(AuthUser authUser, HttpServletRequest request) {

        String refreshToken = refreshTokenRepository.findByUserEmail(authUser.getUser().getEmail());

        if(!(refreshToken != null && jwtUtil.validateToken(refreshToken))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인을 먼저 해야합니다.");
        }

        refreshTokenRepository.delete(authUser.getUser().getEmail());

        String accessToken = jwtUtil.resolveToken(request);

        long expiration = jwtUtil.getExpiration(accessToken);
        refreshTokenRepository.setBlackList(accessToken, "access_token", expiration);

    }


}
