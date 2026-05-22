package com.trustspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private UserResponse user;
    private Long propertyId;
    private Integer rating;
    private String comment;
    private List<String> images;
    private LocalDateTime createdAt;
}
