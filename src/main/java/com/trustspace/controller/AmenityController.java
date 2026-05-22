package com.trustspace.controller;

import com.trustspace.dto.response.AmenityResponse;
import com.trustspace.service.AmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityController {
    
    private final AmenityService amenityService;
    
    @GetMapping
    public ResponseEntity<List<AmenityResponse>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.getAllAmenities());
    }
}
