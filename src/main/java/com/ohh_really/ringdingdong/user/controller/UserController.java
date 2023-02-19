package com.ohh_really.ringdingdong.user.controller;

import com.ohh_really.ringdingdong.user.dto.LoginFormDto;
import com.ohh_really.ringdingdong.user.dto.UserInfoDto;
import com.ohh_really.ringdingdong.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping("/info")
    @Operation(summary = "회원 정보 조회")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 계정")
    public ResponseEntity<UserInfoDto> getUserInfo(
            @Parameter(name = "jwt", description = "유저 토큰") @RequestHeader(value = "jwt") String token
    ) {
        return userService.getUserInfo(token);
    }
}
