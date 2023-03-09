package com.ohh_really.ringdingdong.location.service;

import com.ohh_really.ringdingdong.config.JwtConfig;
import com.ohh_really.ringdingdong.location.entity.Favorite;
import com.ohh_really.ringdingdong.location.entity.Location;
import com.ohh_really.ringdingdong.location.kakaoapi.Document;
import com.ohh_really.ringdingdong.location.kakaoapi.KakaoApi;
import com.ohh_really.ringdingdong.location.kakaoapi.Root;
import com.ohh_really.ringdingdong.location.repository.FavoriteRepository;
import com.ohh_really.ringdingdong.location.repository.LocationRepository;
import com.ohh_really.ringdingdong.user.entity.User;
import com.ohh_really.ringdingdong.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final KakaoApi kakaoApi;
    private final JwtConfig jwtConfig;
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, KakaoApi kakaoApi, JwtConfig jwtConfig,
                           FavoriteRepository favoriteRepository,
                           UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.kakaoApi = kakaoApi;
        this.jwtConfig = jwtConfig;
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ArrayList<Location>> findByCountryAndStateAndCity(String region, String level1, String level2) {
        if (level2 == null) {
            return ResponseEntity.ok(locationRepository.findByRegionAndLevel1(region, level1));
        }
        return ResponseEntity.ok(locationRepository.findByRegionAndLevel1AndLevel2(region, level1, level2));
    }

    public ResponseEntity<Location> updateCurrentLocation(Double x, Double y, String token) {
        // 토큰 검증
        Map<String, Object> claims = jwtConfig.verifyJWT(token);
        Optional<User> user = userRepository.findById((String) claims.get("email"));
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // 카카오 API 호출
        Optional<Document> document = kakaoApi.getRegion(x, y);
        if (document.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // DB에 저장된 위치 정보가 있는지 확인
        Optional<Location> location = locationRepository.findByLevel1AndLevel2AndLevel3AndLevel4(document.get().region1depthName, document.get().region2depthName, document.get().region3depthName, document.get().region4depthName);

        // 없으면 저장
        if (location.isEmpty()) {
            location = Optional.of(locationRepository.save(
                    Location.builder()
                            .region("대한민국")
                            .fullName(document.get().getAddressName())
                            .level1(document.get().getRegion1depthName())
                            .level2(document.get().getRegion2depthName())
                            .level3(document.get().getRegion3depthName())
                            .level4(document.get().getRegion4depthName())
                            .build()));
        }
        // 현재 위치로 설정
        Optional<Favorite> current = favoriteRepository.findByUserAndIsCurrentTrue(user.get());
        if (current.isPresent()) {
            current.get().setLocation(location.get());
            favoriteRepository.save(current.get());
        } else {
            Favorite favorite = Favorite.builder()
                    .location(location.get())
                    .user(user.get())
                    .isCurrent(true)
                    .build();
            favoriteRepository.save(favorite);
        }

        return ResponseEntity.ok(location.get());
    }

    public ResponseEntity<ArrayList<Location>> getFavoriteLocation(String token) {
        Map<String, Object> claims = jwtConfig.verifyJWT(token);
        Optional<User> user = userRepository.findById((String) claims.get("email"));
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ArrayList<Favorite> favorites = favoriteRepository.findByUser(user.get());
        ArrayList<Location> locations = new ArrayList<>();
        Collections.sort(favorites);
        for (Favorite favorite : favorites) {
            locations.add(favorite.getLocation());
        }
        return ResponseEntity.ok(locations);
    }

    public ResponseEntity<Location> addFavoriteLocation(String token, Long FavoriteId) {
        Map<String, Object> claims = jwtConfig.verifyJWT(token);
        Optional<User> user = userRepository.findById((String) claims.get("email"));
        Optional<Location> location = locationRepository.findById(FavoriteId);
        if (user.isEmpty() || location.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Favorite favorite = Favorite.builder()
                .location(location.get())
                .user(user.get())
                .isCurrent(false)
                .build();
        favoriteRepository.save(favorite);
        return ResponseEntity.ok(location.get());
    }

    public ResponseEntity<String> deleteFavoriteLocation(String token, Long FavoriteId) {
        Map<String, Object> claims = jwtConfig.verifyJWT(token);
        Optional<User> user = userRepository.findById((String) claims.get("email"));
        Optional<Favorite> favorite = favoriteRepository.findById(FavoriteId);
        if (user.isEmpty() || favorite.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        favoriteRepository.delete(favorite.get());
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
