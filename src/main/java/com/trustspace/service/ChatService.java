package com.trustspace.service;

import com.trustspace.dto.request.MessageRequest;
import com.trustspace.dto.response.ChatRoomResponse;
import com.trustspace.dto.response.MessageResponse;
import com.trustspace.entity.ChatRoom;
import com.trustspace.entity.Message;
import com.trustspace.entity.Property;
import com.trustspace.entity.User;
import com.trustspace.enums.NotificationType;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.exception.UnauthorizedException;
import com.trustspace.mapper.ChatMapper;
import com.trustspace.repository.ChatRoomRepository;
import com.trustspace.repository.MessageRepository;
import com.trustspace.repository.PropertyRepository;
import com.trustspace.repository.UserRepository;
import com.trustspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final NotificationService notificationService;
    private final ChatMapper chatMapper;
    
    public MessageResponse sendMessage(MessageRequest request) {
        User sender = SecurityUtil.getCurrentUser();
        
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));
        
        // Get or create chat room
        ChatRoom chatRoom = chatRoomRepository.findByUsers(sender.getId(), receiver.getId())
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.builder()
                            .user1(sender)
                            .user2(receiver)
                            .build();
                    
                    if (request.getPropertyId() != null) {
                        Property property = propertyRepository.findById(request.getPropertyId())
                                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
                        newRoom.setProperty(property);
                    }
                    
                    return chatRoomRepository.save(newRoom);
                });
        
        Message message = Message.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .isRead(false)
                .build();
        
        message = messageRepository.save(message);
        
        // Send notification to receiver
        notificationService.createNotification(
                receiver.getId(),
                NotificationType.NEW_MESSAGE,
                "New Message",
                "You have a new message from " + sender.getFullName(),
                "/chat/" + chatRoom.getId()
        );
        
        return chatMapper.toMessageResponse(message);
    }
    
    public List<ChatRoomResponse> getMyChatRooms() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(currentUser.getId());
        
        return chatRooms.stream()
                .map(room -> chatMapper.toChatRoomResponse(room, currentUser.getId()))
                .toList();
    }
    
    public List<MessageResponse> getChatMessages(Long chatRoomId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found"));
        
        // Verify user is part of this chat
        if (!chatRoom.getUser1().getId().equals(currentUser.getId()) &&
            !chatRoom.getUser2().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't have access to this chat");
        }
        
        List<Message> messages = messageRepository.findByChatRoomIdOrderBySentAtAsc(chatRoomId);
        
        return messages.stream()
                .map(chatMapper::toMessageResponse)
                .toList();
    }
    
    public MessageResponse markAsRead(Long messageId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        
        if (!message.getReceiver().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only mark your own messages as read");
        }
        
        message.setIsRead(true);
        message.setReadAt(LocalDateTime.now());
        message = messageRepository.save(message);
        
        return chatMapper.toMessageResponse(message);
    }
    
    public Long getUnreadMessageCount() {
        User currentUser = SecurityUtil.getCurrentUser();
        return messageRepository.countUnreadMessagesByUserId(currentUser.getId());
    }
}
