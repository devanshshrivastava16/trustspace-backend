package com.trustspace.dto.response;

import com.trustspace.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String profileImage;
    private String bio;
    private CityResponse city;
    private Role role;
    private Boolean isVerified;
    private LocalDateTime createdAt;
}
