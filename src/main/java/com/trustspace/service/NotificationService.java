package com.trustspace.service;

import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.NotificationResponse;
import com.trustspace.entity.Notification;
import com.trustspace.entity.User;
import com.trustspace.enums.NotificationType;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.mapper.NotificationMapper;
import com.trustspace.repository.NotificationRepository;
import com.trustspace.repository.UserRepository;
import com.trustspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    
    public void createNotification(Long userId, NotificationType type, String title, 
                                   String message, String link) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .link(link)
                .isRead(false)
                .build();
        
        notificationRepository.save(notification);
    }
    
    public List<NotificationResponse> getMyNotifications() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<Notification> notifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(currentUser.getId());
        
        return notifications.stream()
                .map(notificationMapper::toResponse)
                .toList();
    }
    
    public Page<NotificationResponse> getMyNotificationsPaginated(int page, int size) {
        User currentUser = SecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Notification> notifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(currentUser.getId(), pageable);
        
        return notifications.map(notificationMapper::toResponse);
    }
    
    public NotificationResponse markAsRead(Long notificationId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        
        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Notification not found");
        }
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        
        return notificationMapper.toResponse(notification);
    }
    
    public ApiResponse markAllAsRead() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<Notification> notifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(currentUser.getId());
        
        notifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        });
        
        notificationRepository.saveAll(notifications);
        
        return ApiResponse.builder()
                .success(true)
                .message("All notifications marked as read")
                .build();
    }
    
    public Long getUnreadCount() {
        User currentUser = SecurityUtil.getCurrentUser();
        return notificationRepository.countUnreadByUserId(currentUser.getId());
    }
    
    public ApiResponse deleteNotification(Long notificationId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        
        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Notification not found");
        }
        
        notificationRepository.delete(notification);
        
        return ApiResponse.builder()
                .success(true)
                .message("Notification deleted successfully")
                .build();
    }
}
