package com.ohh_really.ringdingdong.location.service;

import com.ohh_really.ringdingdong.location.entity.Location;
import com.ohh_really.ringdingdong.location.repository.LocationRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public ResponseEntity<ArrayList<Location>> findByCountryAndStateAndCity(String country, String state, String city) {
        return ResponseEntity.ok(locationRepository.findByCountryAndStateAndCity(country, state, city));
    }

}
