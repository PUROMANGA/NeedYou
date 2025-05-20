package com.example.shopingplusassignment.domain.config;

import com.example.shopingplusassignment.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    @Value("${jwt.token-time}")
    private long tokenTime;

    @Value("${jwt.secret.key}")
    private String securityKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(securityKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * 토큰 생성
     * @param userId
     * @param name
     * @param email
     * @param userRole
     * @return
     */
    public String createToken(Long userId, String name, String email, UserRole userRole){
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim("userId", userId)
                        .claim("name", name)
                        .claim("userRole", userRole)
                        .setExpiration(new Date(date.getTime() + tokenTime))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * 토큰 유효성 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            log.warn("JWT 유효성 검사 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 토큰 추출
     * @param request
     * @return
     * @throws ServerException
     */
    public String resolveToken(HttpServletRequest request) throws ServerException {
        String bearer = request.getHeader("Authorization");
        if(StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)){
            return bearer.substring(BEARER_PREFIX.length());
        }
        throw new ServerException("Not Found Token");
    }

    /**
     * 사용자 정보 추출
     * @param token
     * @return
     */
    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
