package com.trustspace.mapper;

import com.trustspace.dto.response.PropertyResponse;
import com.trustspace.entity.Property;
import com.trustspace.repository.WishlistRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {CityMapper.class, UserMapper.class, PropertyImageMapper.class, AmenityMapper.class})
public abstract class PropertyMapper {
    
    @Autowired
    protected WishlistRepository wishlistRepository;
    
    @Mapping(target = "images", source = "property.images")
    @Mapping(target = "amenities", expression = "java(property.getPropertyAmenities().stream().map(pa -> amenityMapper.toResponse(pa.getAmenity())).toList())")
    @Mapping(target = "averageRating", expression = "java(property.getAverageRating())")
    @Mapping(target = "totalReviews", expression = "java(property.getTotalReviews())")
    @Mapping(target = "isWishlisted", expression = "java(userId != null && wishlistRepository.existsByUserIdAndPropertyId(userId, property.getId()))")
    public abstract PropertyResponse toResponse(Property property, Long userId);
    
    @Autowired
    protected AmenityMapper amenityMapper;
}
