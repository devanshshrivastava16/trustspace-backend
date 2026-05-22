package com.trustspace.service;

import com.trustspace.dto.request.ChangePasswordRequest;
import com.trustspace.dto.request.UpdateProfileRequest;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.UserResponse;
import com.trustspace.entity.User;
import com.trustspace.exception.BadRequestException;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.exception.UnauthorizedException;
import com.trustspace.mapper.UserMapper;
import com.trustspace.repository.CityRepository;
import com.trustspace.repository.UserRepository;
import com.trustspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    public UserResponse getCurrentUser() {
        User user = SecurityUtil.getCurrentUser();
        return userMapper.toResponse(user);
    }
    
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.toResponse(user);
    }
    
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = SecurityUtil.getCurrentUser();
        
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        
        if (request.getCityId() != null) {
            user.setCity(cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("City not found")));
        }
        
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }
        
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }
    
    public UserResponse uploadProfileImage(MultipartFile file) {
        User user = SecurityUtil.getCurrentUser();
        
        // Delete old image if exists
        if (user.getProfileImage() != null) {
            try {
                String publicId = extractPublicIdFromUrl(user.getProfileImage());
                cloudinaryService.deleteImage(publicId);
            } catch (Exception e) {
                log.warn("Failed to delete old profile image", e);
            }
        }
        
        // Upload new image
        Map<String, String> uploadResult = cloudinaryService.uploadImage(file, "profiles");
        user.setProfileImage(uploadResult.get("imageUrl"));
        
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }
    
    public ApiResponse changePassword(ChangePasswordRequest request) {
        User user = SecurityUtil.getCurrentUser();
        
        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        
        // Set new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        return ApiResponse.builder()
                .success(true)
                .message("Password changed successfully")
                .build();
    }
    
    public ApiResponse deleteAccount() {
        User user = SecurityUtil.getCurrentUser();
        
        // Delete profile image if exists
        if (user.getProfileImage() != null) {
            try {
                String publicId = extractPublicIdFromUrl(user.getProfileImage());
                cloudinaryService.deleteImage(publicId);
            } catch (Exception e) {
                log.warn("Failed to delete profile image", e);
            }
        }
        
        userRepository.delete(user);
        
        return ApiResponse.builder()
                .success(true)
                .message("Account deleted successfully")
                .build();
    }
    
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }
    
    private String extractPublicIdFromUrl(String imageUrl) {
        // Extract public_id from Cloudinary URL
        // Example: https://res.cloudinary.com/demo/image/upload/v1234567890/trustspace/profiles/abc123.jpg
        // Returns: trustspace/profiles/abc123
        String[] parts = imageUrl.split("/upload/");
        if (parts.length > 1) {
            String path = parts[1];
            int versionIndex = path.indexOf("/");
            if (versionIndex > 0) {
                path = path.substring(versionIndex + 1);
            }
            return path.substring(0, path.lastIndexOf('.'));
        }
        return null;
    }
}
