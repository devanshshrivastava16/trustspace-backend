package com.trustspace.controller;

import com.trustspace.dto.request.ChangePasswordRequest;
import com.trustspace.dto.request.UpdateProfileRequest;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.UserResponse;
import com.trustspace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
    
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }
    
    @PostMapping("/profile/image")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.uploadProfileImage(file));
    }
    
    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }
    
    @DeleteMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteAccount() {
        return ResponseEntity.ok(userService.deleteAccount());
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
