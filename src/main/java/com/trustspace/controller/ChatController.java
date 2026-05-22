package com.trustspace.controller;

import com.trustspace.dto.request.MessageRequest;
import com.trustspace.dto.response.ChatRoomResponse;
import com.trustspace.dto.response.MessageResponse;
import com.trustspace.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ChatController {
    
    private final ChatService chatService;
    
    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.sendMessage(request));
    }
    
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getMyChatRooms() {
        return ResponseEntity.ok(chatService.getMyChatRooms());
    }
    
    @GetMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<List<MessageResponse>> getChatMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatService.getChatMessages(chatRoomId));
    }
    
    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<MessageResponse> markAsRead(@PathVariable Long messageId) {
        return ResponseEntity.ok(chatService.markAsRead(messageId));
    }
    
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadMessageCount() {
        return ResponseEntity.ok(chatService.getUnreadMessageCount());
    }
}
