package com.trustspace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long id;
    private UserResponse user1;
    private UserResponse user2;
    private PropertyResponse property;
    private MessageResponse lastMessage;
    private Long unreadCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
