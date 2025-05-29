package com.example.shopingplusassignment.domain.kakao.client;

import com.example.shopingplusassignment.domain.kakao.dto.KakaoTokenResponse;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoClient {

    private final RestClient restClient;
    private final UserRepository userRepository;

    private final static String AUTH_SERVER_BASE_URL = "https://kauth.kakao.com";
    private final static String RESOURCE_SERVER_BASE_URL = "https://kapi.kakao.com";

    @Value("${oauth2.kakao.client_id}")
    private String clientId;
    @Value("${oauth2.kakao.redirect_url}")
    private String redirectUrl;

    public String generateLoginPageUrl() {
        return AUTH_SERVER_BASE_URL
                + "/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUrl
                + "&response_type=" + "code";
    }

    public String getAccessToken(String authorizationCode) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("code", authorizationCode);

        return restClient.post()
                .uri(AUTH_SERVER_BASE_URL + "/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, resp) -> {
                    throw new RuntimeException("카카오 AccessToken 조회 실패");
                })
                .body(KakaoTokenResponse.class)
                .getAccessToken();
    }

    public KakaoUserInfo kakaoUserInfo(String accessToken) {
        KakaoResponse response = restClient.get()
                .uri(RESOURCE_SERVER_BASE_URL + "/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, resp) -> {
                    throw new RuntimeException("카카오 UserInfo 조회 실패");
                })
                .body(KakaoResponse.class);
        return response.getKakaoUserInfo();
    }

    @Transactional
    public User registerIfAbsent(KakaoUserInfo userInfo) {
        return userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> {
                    User user = User.builder()
                            .name(userInfo.getName())
                            .email(userInfo.getEmail())
                            .phone(userInfo.getPhone())
                            .userRole(userInfo.getUserRole())
                            .build();
                    return userRepository.save(user);
                });
    }
}
