package com.ohh_really.ringdingdong.user.service;

import com.ohh_really.ringdingdong.user.dto.UserRegisterForm;
import com.ohh_really.ringdingdong.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> register(UserRegisterForm userRegisterForm) {
        userRepository.existsByUserId(userRegisterForm.getUserId());

        if (userRepository.existsByUserId(userRegisterForm.getUserId())) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        return ResponseEntity.ok("User registered");
    }


}
