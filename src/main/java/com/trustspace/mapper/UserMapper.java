package com.trustspace.mapper;

import com.trustspace.dto.response.UserResponse;
import com.trustspace.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CityMapper.class})
public interface UserMapper {
    
    @Mapping(target = "city", source = "city")
    UserResponse toResponse(User user);
}
