package com.example.shopingplusassignment.domain.auth.service;

import com.example.shopingplusassignment.domain.auth.dto.request.LoginRequestDto;
import com.example.shopingplusassignment.domain.auth.dto.request.SignupRequestDto;
import com.example.shopingplusassignment.domain.auth.dto.response.LoginResponseDto;
import com.example.shopingplusassignment.domain.auth.dto.response.SignupResponseDto;
import com.example.shopingplusassignment.domain.config.JwtUtil;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponseDto signup(SignupRequestDto requestDto) {

        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일이 올바르지 않습니다.");
        }

        String encodePassword = passwordEncoder.encode(requestDto.getPassword());
        UserRole userRole = UserRole.of(requestDto.getUserRole());


        User user = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                encodePassword,
                requestDto.getPhone(),
                userRole
        );

        User saveUser = userRepository.save(user);
        String bearerToken = jwtUtil.createToken(saveUser.getId(), saveUser.getName(), saveUser.getEmail(), userRole);

        return new SignupResponseDto(
                saveUser.getId(),
                saveUser.getName(),
                saveUser.getEmail(),
                saveUser.getPhone(),
                saveUser.getUserRole(),
                saveUser.getCreatTime(),
                saveUser.getModifiedTime(),
                bearerToken
        );
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist email = " + requestDto.getEmail()));

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getName(), user.getEmail(), user.getUserRole());

        return new LoginResponseDto(bearerToken);
    }
}
