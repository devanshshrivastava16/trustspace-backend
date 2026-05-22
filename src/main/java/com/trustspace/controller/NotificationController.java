package com.trustspace.controller;

import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.NotificationResponse;
import com.trustspace.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }
    
    @GetMapping("/paginated")
    public ResponseEntity<Page<NotificationResponse>> getMyNotificationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(notificationService.getMyNotificationsPaginated(page, size));
    }
    
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }
    
    @PutMapping("/mark-all-read")
    public ResponseEntity<ApiResponse> markAllAsRead() {
        return ResponseEntity.ok(notificationService.markAllAsRead());
    }
    
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        return ResponseEntity.ok(notificationService.getUnreadCount());
    }
    
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.deleteNotification(notificationId));
    }
}
