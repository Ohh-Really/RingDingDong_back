package com.ohh_really.ringdingdong.user.controller;

import com.ohh_really.ringdingdong.user.dto.FcmTokenDto;
import com.ohh_really.ringdingdong.user.dto.GoogleLoginFormDto;
import com.ohh_really.ringdingdong.user.dto.LoginFormDto;
import com.ohh_really.ringdingdong.user.dto.UserInfoDto;
import com.ohh_really.ringdingdong.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "User", description = "사용자 관련 API")
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
    @ApiResponse(responseCode = "201", description = "구글 회원가입 성공 & 정책 미동의 계정")
    @ApiResponse(responseCode = "202", description = "정책 미동의 계정")
    @ApiResponse(responseCode = "400", description = "구글 회원가입 실패")
    @ApiResponse(responseCode = "404", description = "id가 일치하지 않음")
    public ResponseEntity<String> login(
            @RequestBody GoogleLoginFormDto googleLoginFormDto
    ) {
        return userService.login(googleLoginFormDto);
    }

    @PostMapping("/policyAgree")
    @Operation(summary = "개인정보 수집 및 이용 동의")
    @ApiResponse(responseCode = "200", description = "개인정보 수집 및 이용 동의 성공")
    @ApiResponse(responseCode = "202", description = "이미 동의한 계정")
    public ResponseEntity<String> policyAgree(
            @Parameter(name = "jwt", description = "유저 토큰") @RequestHeader(value = "jwt") String token
    ) {
        return userService.policyAgree(token);
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

    @PostMapping("/fcmToken")
    @Operation(summary = "토큰 업데이트")
    public ResponseEntity<String> setFcmToken(
            @Parameter(name = "jwt", description = "유저 토큰") @RequestHeader(value = "jwt") String token,
            @RequestBody FcmTokenDto fcmTokenDto
    ) {
        return userService.updateFcmToken(token, fcmTokenDto);
    }
}
