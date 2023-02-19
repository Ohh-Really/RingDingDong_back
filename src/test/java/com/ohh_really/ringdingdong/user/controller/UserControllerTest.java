package com.ohh_really.ringdingdong.user.controller;

import com.ohh_really.ringdingdong.user.dto.GoogleLoginFormDto;
import com.ohh_really.ringdingdong.user.dto.UserInfoDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    @Transactional
    void 첫_로그인() {
        GoogleLoginFormDto googleLoginFormDto = GoogleLoginFormDto.builder()
                .displayName("Test User")
                .email("test1@test.com")
                .id("165165168468168435180")
                .photoUrl("https://lh3.googleusercontent.com/a/elsioufnhclesslidugUIOHLIUHlhoinQGD0A3g")
                .build();

        ResponseEntity<String> response = userController.login(googleLoginFormDto);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    @Transactional
    void 재_로그인_정책동의_X() {
        GoogleLoginFormDto googleLoginFormDto = GoogleLoginFormDto.builder()
                .displayName("Test User")
                .email("test1@test.com")
                .id("165165168468168435180")
                .photoUrl("https://lh3.googleusercontent.com/a/elsioufnhclesslidugUIOHLIUHlhoinQGD0A3g")
                .build();

        ResponseEntity<String> firstResponse = userController.login(googleLoginFormDto);
        ResponseEntity<String> secondResponse = userController.login(googleLoginFormDto);

        assertEquals(201, firstResponse.getStatusCodeValue());
        assertEquals(202, secondResponse.getStatusCodeValue());
    }

    @Test
    @Transactional
    void 재_로그인_정책동의_O() {
        GoogleLoginFormDto googleLoginFormDto = GoogleLoginFormDto.builder()
                .displayName("Test User")
                .email("test1@test.com")
                .id("165165168468168435180")
                .photoUrl("https://lh3.googleusercontent.com/a/elsioufnhclesslidugUIOHLIUHlhoinQGD0A3g")
                .build();

        ResponseEntity<String> firstResponse = userController.login(googleLoginFormDto);
        userController.policyAgree(firstResponse.getBody());
        ResponseEntity<String> secondResponse = userController.login(googleLoginFormDto);

        assertEquals(201, firstResponse.getStatusCodeValue());
        assertEquals(200, secondResponse.getStatusCodeValue());
    }

    @Test
    @Transactional
    void 로그인_ID없이() {
        GoogleLoginFormDto googleLoginFormDto = GoogleLoginFormDto.builder()
                .displayName("Test User")
                .email("test1@test.com")
                .photoUrl("https://lh3.googleusercontent.com/a/elsioufnhclesslidugUIOHLIUHlhoinQGD0A3g")
                .build();

        ResponseEntity<String> response = userController.login(googleLoginFormDto);
        assertEquals(400, response.getStatusCodeValue());
    }


    @Test
    @Transactional
    void 유저_정보_획득() {
        GoogleLoginFormDto googleLoginFormDto = GoogleLoginFormDto.builder()
                .displayName("Test User")
                .email("test1@test.com")
                .id("165165168468168435180")
                .photoUrl("https://lh3.googleusercontent.com/a/elsioufnhclesslidugUIOHLIUHlhoinQGD0A3g")
                .build();

        ResponseEntity<String> response = userController.login(googleLoginFormDto);
        UserInfoDto userInfo = userController.getUserInfo(response.getBody()).getBody();

        assert userInfo != null;
        assertEquals(googleLoginFormDto.getDisplayName(), userInfo.getUsername());
        assertEquals(googleLoginFormDto.getEmail(), userInfo.getEmail());
        assertEquals(googleLoginFormDto.getPhotoUrl(), userInfo.getPicture());
        assertEquals(googleLoginFormDto.getId(), userInfo.getId());
    }

    @Test
    @Transactional
    void 정책동의_테스트() {
        GoogleLoginFormDto googleLoginFormDto = GoogleLoginFormDto.builder()
                .displayName("Test User")
                .email("test1@test.com")
                .id("165165168468168435180")
                .photoUrl("https://lh3.googleusercontent.com/a/elsioufnhclesslidugUIOHLIUHlhoinQGD0A3g")
                .build();

        ResponseEntity<String> response = userController.login(googleLoginFormDto);
        String token = response.getBody();
        UserInfoDto firstUserInfo = userController.getUserInfo(token).getBody();

        assert firstUserInfo != null;
        assertFalse(firstUserInfo.isEnabled());

        userController.policyAgree(token);
        UserInfoDto secondUserInfo = userController.getUserInfo(token).getBody();

        assert secondUserInfo != null;
        assertTrue(secondUserInfo.isEnabled());

    }
}
