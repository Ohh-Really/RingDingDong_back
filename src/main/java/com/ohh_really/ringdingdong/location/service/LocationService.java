package com.ohh_really.ringdingdong.location.service;

import com.ohh_really.ringdingdong.config.JwtConfig;
import com.ohh_really.ringdingdong.location.entity.Location;
import com.ohh_really.ringdingdong.location.kakaoapi.KakaoApi;
import com.ohh_really.ringdingdong.location.kakaoapi.Root;
import com.ohh_really.ringdingdong.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final KakaoApi kakaoApi;
    private final JwtConfig jwtConfig;

    @Autowired
    public LocationService(LocationRepository locationRepository, KakaoApi kakaoApi , JwtConfig jwtConfig) {
        this.locationRepository = locationRepository;
        this.kakaoApi = kakaoApi;
        this.jwtConfig = jwtConfig;
    }

    public ResponseEntity<ArrayList<Location>> findByCountryAndStateAndCity(String region, String region1, String region2) {
        if (region2 == null) {
            return ResponseEntity.ok(locationRepository.findByRegionAndRegion1(region, region1));
        }
        return ResponseEntity.ok(locationRepository.findByRegionAndRegion1AndRegion2(region, region1, region2));
    }

    public ResponseEntity<Root> updateLocation(Double x, Double y, String token) {
        jwtConfig.verifyJWT(token);
        return ResponseEntity.ok(kakaoApi.getGeoCode(x, y));
    }

}
