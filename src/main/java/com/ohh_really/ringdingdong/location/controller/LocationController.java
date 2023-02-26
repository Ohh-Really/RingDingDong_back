package com.ohh_really.ringdingdong.location.controller;

import com.ohh_really.ringdingdong.location.entity.Location;
import com.ohh_really.ringdingdong.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam String country,
            @RequestParam String state,
            @RequestParam String city
    ) {
        return locationService.findByCountryAndStateAndCity(country, state, city);
    }
}
