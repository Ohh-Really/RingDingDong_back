package com.ohh_really.ringdingdong.user.service;

import com.ohh_really.ringdingdong.config.JwtConfig;
import com.ohh_really.ringdingdong.user.dto.LoginFormDto;
import com.ohh_really.ringdingdong.user.entity.User;
import com.ohh_really.ringdingdong.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    @Autowired
    public UserService(UserRepository userRepository, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
    }

    public ResponseEntity<String> login(LoginFormDto loginFormDto) {
        Optional<User> user = userRepository.findById(loginFormDto.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (!user.get().getId().equals(loginFormDto.getId())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        } else if (!user.get().isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("정책 비동의 계정입니다.");
        } else {
            return ResponseEntity.ok(jwtConfig.createToken(user.get()));
        }
    }

    public ResponseEntity<String> policyAgree(LoginFormDto loginFormDto) {
        Optional<User> user = userRepository.findById(loginFormDto.getEmail());

        if (user.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else if (!user.get().getId().equals(loginFormDto.getId())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        } else if (user.get().isEnabled()) {
            return ResponseEntity.accepted().body("이미 활성화 된 계정입니다.");
        } else {
            user.get().setEnabled(true);
            userRepository.save(user.get());
            return ResponseEntity.ok(jwtConfig.createToken(user.get()));
        }
    }


}
