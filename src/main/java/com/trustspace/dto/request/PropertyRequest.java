package com.trustspace.dto.request;

import com.trustspace.enums.PropertyType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    @NotNull(message = "Property type is required")
    private PropertyType propertyType;
    
    @NotNull(message = "City is required")
    private Long cityId;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private Double latitude;
    private Double longitude;
    
    @NotNull(message = "Hourly price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Hourly price must be greater than 0")
    private BigDecimal hourlyPrice;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Daily price must be greater than 0")
    private BigDecimal dailyPrice;
    
    @Min(value = 0, message = "Bedrooms cannot be negative")
    private Integer bedrooms;
    
    @Min(value = 0, message = "Bathrooms cannot be negative")
    private Integer bathrooms;
    
    private Boolean parkingAvailable;
    private Boolean furnished;
    
    @NotNull(message = "Guest capacity is required")
    @Min(value = 1, message = "Guest capacity must be at least 1")
    private Integer guestCapacity;
    
    @Min(value = 0, message = "Size cannot be negative")
    private Double size;
    
    private List<Long> amenityIds;
}
