package com.trustspace.mapper;

import com.trustspace.dto.response.PropertyImageResponse;
import com.trustspace.entity.PropertyImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyImageMapper {
    
    PropertyImageResponse toResponse(PropertyImage propertyImage);
}
