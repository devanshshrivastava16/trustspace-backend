package com.trustspace.controller;

import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.PropertyResponse;
import com.trustspace.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class WishlistController {
    
    private final WishlistService wishlistService;
    
    @PostMapping("/{propertyId}")
    public ResponseEntity<ApiResponse> addToWishlist(@PathVariable Long propertyId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(propertyId));
    }
    
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<ApiResponse> removeFromWishlist(@PathVariable Long propertyId) {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(propertyId));
    }
    
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getMyWishlist() {
        return ResponseEntity.ok(wishlistService.getMyWishlist());
    }
    
    @GetMapping("/{propertyId}/check")
    public ResponseEntity<Boolean> isInWishlist(@PathVariable Long propertyId) {
        return ResponseEntity.ok(wishlistService.isInWishlist(propertyId));
    }
}
