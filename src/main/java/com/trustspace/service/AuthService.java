package com.trustspace.service;

import com.trustspace.dto.request.*;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.AuthResponse;
import com.trustspace.entity.User;
import com.trustspace.enums.Role;
import com.trustspace.exception.BadRequestException;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.exception.UnauthorizedException;
import com.trustspace.repository.CityRepository;
import com.trustspace.repository.UserRepository;
import com.trustspace.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {
    
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        
        // Create new user
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.USER)
                .isVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationTokenExpiry(LocalDateTime.now().plusHours(24))
                .build();
        
        // Set city if provided
        if (request.getCityId() != null) {
            user.setCity(cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("City not found")));
        }
        
        user = userRepository.save(user);
        
        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
        
        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .isVerified(user.getIsVerified())
                .build();
    }
    
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        // Get user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .isVerified(user.getIsVerified())
                .build();
    }
    
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        try {
            String email = jwtUtil.extractUsername(request.getRefreshToken());
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
            
            String newAccessToken = jwtUtil.generateAccessToken(email);
            String newRefreshToken = jwtUtil.generateRefreshToken(email);
            
            return AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .userId(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .isVerified(user.getIsVerified())
                    .build();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }
    
    public ApiResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));
        
        if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification token has expired");
        }
        
        user.setIsVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);
        
        return ApiResponse.builder()
                .success(true)
                .message("Email verified successfully")
                .build();
    }
    
    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
        
        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
        
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        
        return ApiResponse.builder()
                .success(true)
                .message("Password reset email sent")
                .build();
    }
    
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));
        
        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);
        
        return ApiResponse.builder()
                .success(true)
                .message("Password reset successfully")
                .build();
    }
}
