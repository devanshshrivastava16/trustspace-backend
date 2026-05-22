package com.trustspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyImageResponse {
    private Long id;
    private String imageUrl;
    private String publicId;
    private Boolean isPrimary;
    private Integer displayOrder;
}
