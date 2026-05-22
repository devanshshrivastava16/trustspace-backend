package com.trustspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponse {
    private Long id;
    private String name;
    private String state;
    private String country;
    private String imageUrl;
    private Boolean isTrending;
    private Long propertyCount;
}
