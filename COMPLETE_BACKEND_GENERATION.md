# TrustSpace Backend - Complete File Structure

This document outlines all the files that need to be created for the complete TrustSpace backend.

## ✅ Already Created Files

### Entities (13/13)
- User.java
- Property.java
- City.java
- PropertyImage.java
- Amenity.java
- PropertyAmenity.java
- Booking.java
- Review.java
- Wishlist.java
- AvailabilitySlot.java
- ChatRoom.java
- Message.java
- Notification.java

### Enums (6/6)
- Role.java
- PropertyType.java
- PropertyStatus.java
- BookingStatus.java
- PaymentStatus.java
- NotificationType.java

### Repositories (13/13)
- UserRepository.java
- PropertyRepository.java
- CityRepository.java
- BookingRepository.java
- ReviewRepository.java
- WishlistRepository.java
- ChatRoomRepository.java
- MessageRepository.java
- NotificationRepository.java
- AmenityRepository.java
- PropertyImageRepository.java
- PropertyAmenityRepository.java
- AvailabilitySlotRepository.java

### Exception Handling (4/4)
- ResourceNotFoundException.java
- BadRequestException.java
- UnauthorizedException.java
- GlobalExceptionHandler.java

### Security (3/3)
- JwtUtil.java
- JwtAuthenticationFilter.java
- CustomUserDetailsService.java

### Configuration (1/1)
- SecurityConfig.java

### DTOs - Request (11/11)
- RegisterRequest.java
- LoginRequest.java
- PropertyRequest.java
- BookingRequest.java
- ReviewRequest.java
- MessageRequest.java
- UpdateProfileRequest.java
- ChangePasswordRequest.java
- ForgotPasswordRequest.java
- ResetPasswordRequest.java
- RefreshTokenRequest.java

### DTOs - Response (12/12)
- AuthResponse.java
- UserResponse.java
- CityResponse.java
- PropertyResponse.java
- PropertyImageResponse.java
- AmenityResponse.java
- BookingResponse.java
- ReviewResponse.java
- MessageResponse.java
- ChatRoomResponse.java
- NotificationResponse.java
- ApiResponse.java

### Services (2/10)
- AuthService.java ✓
- EmailService.java ✓
- UserService.java (needed)
- PropertyService.java (needed)
- BookingService.java (needed)
- ReviewService.java (needed)
- WishlistService.java (needed)
- ChatService.java (needed)
- NotificationService.java (needed)
- CloudinaryService.java (needed)

## 📝 Files Still Needed

### Services
1. UserService.java
2. PropertyService.java
3. BookingService.java
4. ReviewService.java
5. WishlistService.java
6. ChatService.java
7. NotificationService.java
8. CloudinaryService.java
9. CityService.java
10. AmenityService.java

### Controllers
1. AuthController.java
2. UserController.java
3. PropertyController.java
4. BookingController.java
5. ReviewController.java
6. WishlistController.java
7. ChatController.java
8. NotificationController.java
9. CityController.java
10. AmenityController.java
11. AdminController.java

### WebSocket
1. WebSocketConfig.java
2. ChatWebSocketHandler.java

### Config
1. CloudinaryConfig.java
2. AsyncConfig.java
3. CorsConfig.java (if separate from SecurityConfig)

### Util
1. SecurityUtil.java (for getting current user)
2. FileUploadUtil.java

### MapStruct Mappers
1. UserMapper.java
2. PropertyMapper.java
3. BookingMapper.java
4. ReviewMapper.java
5. ChatMapper.java
6. NotificationMapper.java

### Additional Files
1. .gitignore
2. .env.example
3. README.md
4. Dockerfile
5. docker-compose.yml
6. API_DOCUMENTATION.md

