package com.ohh_really.ringdingdong.location.controller;

import com.ohh_really.ringdingdong.location.entity.Favorite;
import com.ohh_really.ringdingdong.location.entity.Location;
import com.ohh_really.ringdingdong.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/location")
@Tag(name = "Location", description = "Location API")
public class LocationController {
    private final LocationService locationService;
    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/find")
    @Operation(summary = "Find location by country, state and city")
    public ResponseEntity<ArrayList<Location>> find(
            @RequestParam String region,
            @RequestParam String level1,
            @RequestParam(required = false) String level2
    ) {
        return locationService.findByCountryAndStateAndCity(region, level1, level2);
    }

    @PostMapping("/update-location")
    @Operation(summary = "Update location by gps")
    public ResponseEntity<Location> updateLocation(
            @RequestParam Double x,
            @RequestParam Double y,
            @Parameter(name = "jwt", description = "유저 토큰") @RequestHeader(value = "jwt") String token
    ) {
        return locationService.updateCurrentLocation(x, y, token);
    }

    @GetMapping("/favorite")
    @Operation(summary = "Get favorite location")
    public ResponseEntity<ArrayList<Favorite>> getFavoriteLocaion(
            @Parameter(name = "jwt", description = "유저 토큰") @RequestHeader(value = "jwt") String token
    ){
        return locationService.getFavoriteLocation(token);
    }
}
