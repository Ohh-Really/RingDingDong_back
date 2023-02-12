package com.ohh_really.ringdingdong.user.controller;

import com.ohh_really.ringdingdong.user.dto.AuthorizationCode;
import com.ohh_really.ringdingdong.user.service.GoogleOAuth2Service;
import com.ohh_really.ringdingdong.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<AuthorizationCode> googleRedirect(
            @RequestParam("code") String code,
            @RequestParam("scope") String scope,
            @RequestParam("authuser") String authuser,
            @RequestParam("prompt") String prompt
    ) {
        return googleOAuth2Service.startWithGoogle(code);
    }
}
