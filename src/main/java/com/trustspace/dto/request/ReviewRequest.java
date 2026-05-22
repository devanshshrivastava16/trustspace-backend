package com.trustspace.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {
    
    @NotNull(message = "Property ID is required")
    private Long propertyId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;
    
    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    private String comment;
    
    private Long bookingId;
    private List<String> images;
}
