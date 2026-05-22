package com.trustspace.dto.response;

import com.trustspace.enums.PropertyStatus;
import com.trustspace.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyResponse {
    private Long id;
    private String title;
    private String description;
    private PropertyType propertyType;
    private CityResponse city;
    private String address;
    private Double latitude;
    private Double longitude;
    private BigDecimal hourlyPrice;
    private BigDecimal dailyPrice;
    private Integer bedrooms;
    private Integer bathrooms;
    private Boolean parkingAvailable;
    private Boolean furnished;
    private Integer guestCapacity;
    private Double size;
    private PropertyStatus status;
    private UserResponse owner;
    private List<PropertyImageResponse> images;
    private List<AmenityResponse> amenities;
    private Double averageRating;
    private Integer totalReviews;
    private Boolean isWishlisted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
