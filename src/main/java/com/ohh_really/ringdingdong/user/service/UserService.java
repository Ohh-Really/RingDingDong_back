package com.ohh_really.ringdingdong.user.service;

import com.ohh_really.ringdingdong.config.JwtConfig;
import com.ohh_really.ringdingdong.user.UserRole;
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
        Optional<User> user = userRepository.findById(googleLoginFormDto.getEmail());
        if (user.isEmpty()) {
            return registerWithGoogleUser(googleLoginFormDto);
        } else if (!user.get().getId().equals(googleLoginFormDto.getId())) {
            return ResponseEntity.badRequest().body("id가 일치하지 않습니다.");
        } else if (!user.get().isEnabled()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(jwtConfig.createToken(user.get()));
        } else {
            return ResponseEntity.ok(jwtConfig.createToken(user.get()));
        }
    }

    public ResponseEntity<String> registerWithGoogleUser(GoogleLoginFormDto googleLoginFormDto) {
        if (googleLoginFormDto.getDisplayName() == null) {
            return ResponseEntity.badRequest().body("이름이 없습니다.");
        }
        if (googleLoginFormDto.getEmail() == null) {
            return ResponseEntity.badRequest().body("이메일이 없습니다.");
        }
        if (googleLoginFormDto.getId() == null) {
            return ResponseEntity.badRequest().body("id가 없습니다.");
        }

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
}
