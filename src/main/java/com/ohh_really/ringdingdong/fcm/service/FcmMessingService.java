package com.ohh_really.ringdingdong.fcm.service;

import com.ohh_really.ringdingdong.config.JwtConfig;
import com.ohh_really.ringdingdong.fcm.dto.RegionMessageDto;
import com.ohh_really.ringdingdong.location.entity.Favorite;
import com.ohh_really.ringdingdong.location.repository.FavoriteRepository;
import com.ohh_really.ringdingdong.user.UserRole;
import com.ohh_really.ringdingdong.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Service
public class FcmMessingService {

    @Autowired
    public FcmMessingService(FavoriteRepository favoriteRepository,FcmService fcmService,JwtConfig jwtConfig,UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.fcmService = fcmService;
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
    }
    FavoriteRepository favoriteRepository;
    UserRepository userRepository;
    FcmService fcmService;
    JwtConfig jwtConfig;

    public ResponseEntity<String> sendMessageToCountry(RegionMessageDto regionMessageDto, String token) throws Exception {
        Map<String, Object> claims = jwtConfig.verifyJWT(token);

        Set<UserRole> roles = userRepository.findById((String) claims.get("email")).get().getRoles();

        roles.remove(UserRole.USER);
        if (roles.isEmpty()) {
            return ResponseEntity.badRequest().body("권한이 없습니다.");
        }

        ArrayList<Favorite> targetArray =  favoriteRepository.findByLocation_Region(regionMessageDto.getRegion());

        for (Favorite favorite : targetArray) {
            fcmService.sendMessageTo(favorite.getUser().getFcmToken(),regionMessageDto.getTitle(),regionMessageDto.getBody());
        }

        return ResponseEntity.ok("메시지 전송 성공");
    }






}
