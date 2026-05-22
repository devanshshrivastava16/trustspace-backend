package com.trustspace.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;
    
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
    
    private Long cityId;
    private String profileImage;
}
