package com.trustspace.mapper;

import com.trustspace.dto.response.ReviewResponse;
import com.trustspace.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ReviewMapper {
    
    @Mapping(target = "propertyId", source = "property.id")
    ReviewResponse toResponse(Review review);
}
