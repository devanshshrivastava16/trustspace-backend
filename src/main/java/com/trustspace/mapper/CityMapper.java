package com.trustspace.mapper;

import com.trustspace.dto.response.CityResponse;
import com.trustspace.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityMapper {
    
    @Mapping(target = "propertyCount", expression = "java(city.getProperties() != null ? (long) city.getProperties().size() : 0L)")
    CityResponse toResponse(City city);
}
