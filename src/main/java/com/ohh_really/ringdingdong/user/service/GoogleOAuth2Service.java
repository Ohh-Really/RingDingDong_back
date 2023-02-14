package com.ohh_really.ringdingdong.user.service;

import com.ohh_really.ringdingdong.user.UserRole;
import com.ohh_really.ringdingdong.user.dto.AuthorizationCode;
import com.ohh_really.ringdingdong.user.dto.GoogleUserInfo;
import com.ohh_really.ringdingdong.user.dto.UserInfoDto;
import com.ohh_really.ringdingdong.user.entity.User;
import com.ohh_really.ringdingdong.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Set;


@Service
public class GoogleOAuth2Service {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String authorizationGrantType;

    @Autowired
    public GoogleOAuth2Service(UserRepository userRepository, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    public String getGoogleLoginUrl() {
        String url = "https://accounts.google.com/o/oauth2/v2/auth";
        url += "?response_type=code";
        url += "&client_id=" + clientId;
        url += "&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
        url += "&redirect_uri=" + redirectUri;
        return url;
    }

    public ResponseEntity<GoogleUserInfo> startWithGoogle(String code) {
        // 헤더 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", authorizationGrantType);
        params.add("code", code);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());
        // 토큰 발급
        AuthorizationCode authorizationCode = restTemplate.postForObject(
                "https://oauth2.googleapis.com/token",
                request,
                AuthorizationCode.class, params
        );
        // 유저 정보 가져오기
        GoogleUserInfo googleUserInfo = getInfo(authorizationCode.getAccessToken()).getBody();

        // 유저 정보 저장
        this.registerUser(googleUserInfo);

        // 유저 토큰 반환
        return ResponseEntity.ok(googleUserInfo);
    }

    public ResponseEntity<GoogleUserInfo> getInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity request = new HttpEntity(headers);

        return restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                request,
                GoogleUserInfo.class
        );
    }

    public UserInfoDto registerUser(GoogleUserInfo googleUserInfo) {
        User user = User.builder()
                .email(googleUserInfo.getEmail())
                .username(googleUserInfo.getName())
                .enabled(false)
                .accountNonExpired(false)
                .accountNonLocked(false)
                .roles(Set.of(UserRole.USER))
                .picture(googleUserInfo.getPicture())
                .id(googleUserInfo.getId())
                .build();
        return modelMapper.map(userRepository.save(user), UserInfoDto.class);
    }
}
