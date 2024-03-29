package com.ohh_really.ringdingdong.user.service;

import com.ohh_really.ringdingdong.config.JwtConfig;
import com.ohh_really.ringdingdong.user.UserRole;
import com.ohh_really.ringdingdong.user.dto.FcmTokenDto;
import com.ohh_really.ringdingdong.user.dto.GoogleLoginFormDto;
import com.ohh_really.ringdingdong.user.dto.LoginFormDto;
import com.ohh_really.ringdingdong.user.dto.UserInfoDto;
import com.ohh_really.ringdingdong.user.entity.User;
import com.ohh_really.ringdingdong.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, JwtConfig jwtConfig, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<String> login(GoogleLoginFormDto googleLoginFormDto) {

        if (googleLoginFormDto.getEmail() == null || googleLoginFormDto.getId() == null) {
            return ResponseEntity.badRequest().body("이메일 또는 id가 없습니다.");
        }
        boolean update = googleLoginFormDto.getDisplayName() != null || googleLoginFormDto.getPhotoUrl() != null;
        Optional<User> user = userRepository.findById(googleLoginFormDto.getEmail());

        if (user.isEmpty() && update) {
            return registerWithGoogleUser(googleLoginFormDto);
        } else if (user.isPresent() && update) {
            return updateWithGoogleUser(googleLoginFormDto);
        } else if (user.get().getId().equals(googleLoginFormDto.getId())) {
            if (user.get().isEnabled()) {
                return ResponseEntity.ok(jwtConfig.createToken(user.get()));
            } else {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(jwtConfig.createToken(user.get()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
        }
    }

    public ResponseEntity<String> registerWithGoogleUser(GoogleLoginFormDto googleLoginFormDto) {
        User user = User.builder()
                .email(googleLoginFormDto.getEmail())
                .username(googleLoginFormDto.getDisplayName())
                .enabled(false)
                .accountNonExpired(false)
                .accountNonLocked(false)
                .roles(Set.of(UserRole.USER))
                .picture(googleLoginFormDto.getPhotoUrl())
                .id(googleLoginFormDto.getId())
                .build();
        return ResponseEntity.created(null).body(jwtConfig.createToken(userRepository.save(user)));
    }

    public ResponseEntity<String> updateWithGoogleUser(GoogleLoginFormDto googleLoginFormDto) {
        Optional<User> user = userRepository.findById(googleLoginFormDto.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!user.get().getId().equals(googleLoginFormDto.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
        }

        user.get().setUsername(googleLoginFormDto.getDisplayName());
        user.get().setPicture(googleLoginFormDto.getPhotoUrl());

        if (!user.get().isEnabled()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtConfig.createToken(userRepository.save(user.get())));
        }

        return ResponseEntity.status(HttpStatus.OK).body(jwtConfig.createToken(userRepository.save(user.get())));
    }

    public ResponseEntity<String> policyAgree(String token) {
        Map<String, Object> claims = jwtConfig.verifyJWT(token);
        String email = (String) claims.get("email");

        Optional<User> user = userRepository.findById(email);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (user.get().isEnabled()) {
            return ResponseEntity.accepted().body("이미 동의 한 계정입니다.");
        } else {
            user.get().setEnabled(true);
            userRepository.save(user.get());
            return ResponseEntity.ok("정책 동의가 완료되었습니다.");
        }
    }

    public ResponseEntity<UserInfoDto> getUserInfo(String token) {
        Map<String, Object> claims = jwtConfig.verifyJWT(token);
        String email = (String) claims.get("email");

        Optional<User> user = userRepository.findById(email);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                modelMapper.map(user.get(), UserInfoDto.class)
        );
    }

    public ResponseEntity<String> updateFcmToken(String token, FcmTokenDto fcmTokenDto){

        Map<String, Object> claims = jwtConfig.verifyJWT(token);
        String email = (String) claims.get("email");

        Optional<User> user = userRepository.findById(email);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        user.get().setFcmToken(fcmTokenDto.getToken());
        userRepository.save(user.get());
        return ResponseEntity.ok("fcm Token 업데이트 되었습니다.");
    }
}
