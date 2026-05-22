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
public class MessageResponse {
    private Long id;
    private Long chatRoomId;
    private UserResponse sender;
    private UserResponse receiver;
    private String content;
    private Boolean isRead;
    private LocalDateTime sentAt;
}
