package com.trustspace.controller;

import com.trustspace.dto.response.CityResponse;
import com.trustspace.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
    
    private final CityService cityService;
    
    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }
    
    @GetMapping("/trending")
    public ResponseEntity<List<CityResponse>> getTrendingCities() {
        return ResponseEntity.ok(cityService.getTrendingCities());
    }
    
    @GetMapping("/{cityId}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable Long cityId) {
        return ResponseEntity.ok(cityService.getCityById(cityId));
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<CityResponse> getCityByName(@PathVariable String name) {
        return ResponseEntity.ok(cityService.getCityByName(name));
    }
}
