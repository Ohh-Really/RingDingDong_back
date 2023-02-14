package com.ohh_really.ringdingdong.user.controller;

import com.ohh_really.ringdingdong.user.dto.AuthorizationCode;
import com.ohh_really.ringdingdong.user.dto.GoogleUserInfo;
import com.ohh_really.ringdingdong.user.dto.LoginFormDto;
import com.ohh_really.ringdingdong.user.service.GoogleOAuth2Service;
import com.ohh_really.ringdingdong.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final GoogleOAuth2Service googleOAuth2Service;

    @Autowired
    public UserController(GoogleOAuth2Service googleOAuth2Service, UserService userService) {
        this.userService = userService;
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @GetMapping("/google/login")
    @Operation(summary = "Google Login 주소")
    public String googleLogin() {
        return googleOAuth2Service.getGoogleLoginUrl();
    }

    @GetMapping("/google/redirect")
    public ResponseEntity<GoogleUserInfo> googleRedirect(
            @RequestParam("code") String code,
            @RequestParam("scope") String scope,
            @RequestParam("authuser") String authuser,
            @RequestParam("prompt") String prompt
    ) {
        return googleOAuth2Service.startWithGoogle(code);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "403", description = "정책 미동의 계정")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 계정")
    public ResponseEntity<String> login(
            @RequestBody LoginFormDto loginFormDto
    ) {
        return userService.login(loginFormDto);
    }

    @PostMapping("/policyAgree")
    @Operation(summary = "개인정보 수집 및 이용 동의")
    public ResponseEntity<String> policyAgree(
            @RequestBody LoginFormDto loginFormDto
    ) {
        return userService.policyAgree(loginFormDto);
    }
}
