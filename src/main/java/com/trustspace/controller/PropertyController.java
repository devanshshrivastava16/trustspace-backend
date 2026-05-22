package com.trustspace.controller;

import com.trustspace.dto.request.PropertyRequest;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.PropertyResponse;
import com.trustspace.enums.PropertyType;
import com.trustspace.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {
    
    private final PropertyService propertyService;
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.createProperty(request));
    }
    
    @PutMapping("/{propertyId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long propertyId,
            @Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.ok(propertyService.updateProperty(propertyId, request));
    }
    
    @GetMapping("/public/{propertyId}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long propertyId) {
        return ResponseEntity.ok(propertyService.getPropertyById(propertyId));
    }
    
    @GetMapping("/public/city/{cityId}")
    public ResponseEntity<Page<PropertyResponse>> getPropertiesByCity(
            @PathVariable Long cityId,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) Boolean parkingAvailable,
            @RequestParam(required = false) Boolean furnished,
            @RequestParam(defaultValue = "newest") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(propertyService.getPropertiesByCity(
                cityId, propertyType, minPrice, maxPrice, minCapacity, 
                parkingAvailable, furnished, sortBy, page, size));
    }
    
    @GetMapping("/my-properties")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<PropertyResponse>> getMyProperties() {
        return ResponseEntity.ok(propertyService.getMyProperties());
    }
    
    @PostMapping("/{propertyId}/images")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<PropertyResponse> uploadPropertyImages(
            @PathVariable Long propertyId,
            @RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(propertyService.uploadPropertyImages(propertyId, files));
    }
    
    @DeleteMapping("/{propertyId}/images/{imageId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse> deletePropertyImage(
            @PathVariable Long propertyId,
            @PathVariable Long imageId) {
        return ResponseEntity.ok(propertyService.deletePropertyImage(propertyId, imageId));
    }
    
    @DeleteMapping("/{propertyId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse> deleteProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(propertyService.deleteProperty(propertyId));
    }
    
    @GetMapping("/public/search")
    public ResponseEntity<Page<PropertyResponse>> searchProperties(
            @RequestParam(required = false) String keyword,
            @RequestParam Long cityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(propertyService.searchProperties(keyword, cityId, page, size));
    }
}
