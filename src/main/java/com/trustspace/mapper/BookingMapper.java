package com.trustspace.mapper;

import com.trustspace.dto.response.BookingResponse;
import com.trustspace.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, PropertyMapper.class}
)
public interface BookingMapper {

    @Mapping(target = "property", source = "property")
    BookingResponse toResponse(Booking booking);
}