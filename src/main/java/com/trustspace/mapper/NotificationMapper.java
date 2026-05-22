package com.trustspace.mapper;

import com.trustspace.dto.response.NotificationResponse;
import com.trustspace.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    
    NotificationResponse toResponse(Notification notification);
}
