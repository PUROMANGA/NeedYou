package com.example.shopingplusassignment.domain.config;

import com.example.shopingplusassignment.domain.auth.repository.RefreshTokenRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.rmi.ServerException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    @Value("${jwt.accessToken.time}")
    private long accessTokenExpiration;
    @Value("${jwt.refreshToken.time}")
    private long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String securityKey;
    private static Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(securityKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 유저 정보로 AccessToken
    public String createAccessToken(User user) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userRole", user.getUserRole().name())// 토큰 subject 설정
                .setIssuedAt(new Date(now)) // 토큰 생성 시간(현재 시각)
                .setExpiration(new Date(now + accessTokenExpiration)) // 만료 시간 : 1시간
                .signWith(key, signatureAlgorithm) // 비밀키로 서명 (HS256 알고리즘)
                .compact(); // JWT 문자열로 변환
    }

    // 유저 정보로 RefreshToken
    public String createRefreshToken(User user) {
        long now = System.currentTimeMillis(); // 현재 시간 가져오기 -> 발급/만료시간 설정

        return Jwts.builder()
                .setSubject(user.getEmail()) // 토큰 subject 설정
                .claim("userRole", user.getUserRole().name())// 토큰 subject 설정
                .setIssuedAt(new Date(now)) // 토큰 생성 시간(현재 시각)
                .setExpiration(new Date(now + refreshTokenExpiration)) // 만료 시간 : 14일
                .signWith(key, signatureAlgorithm) // 서명 키 + 알고리즘
                .compact(); // JWT 문자열 생성
    }

    /**
     * 토큰 유효성 검사
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !refreshTokenRepository.hasKeyBlackList(token);

        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다."); // 서명 오류
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다."); // 토큰 만료
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다."); // 잘못된 형식
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 비어있습니다."); // 토큰 null
        }
        return false;
    }

    /**
     * 토큰 추출
     *
     * @param request
     * @return
     * @throws ServerException
     */
    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
            return bearer.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * 사용자 정보 추출
     *
     * @param token
     * @return
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 남은 만료시간 구하기
    public long getExpiration(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }
}