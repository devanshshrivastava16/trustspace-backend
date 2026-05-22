package com.trustspace.mapper;

import com.trustspace.dto.response.AmenityResponse;
import com.trustspace.entity.Amenity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    
    AmenityResponse toResponse(Amenity amenity);
}
