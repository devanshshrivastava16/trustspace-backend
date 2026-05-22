package com.trustspace.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {
    
    @NotNull(message = "Receiver ID is required")
    private Long receiverId;
    
    @NotBlank(message = "Message content is required")
    @Size(max = 2000, message = "Message must not exceed 2000 characters")
    private String content;
    
    private Long propertyId;
}
