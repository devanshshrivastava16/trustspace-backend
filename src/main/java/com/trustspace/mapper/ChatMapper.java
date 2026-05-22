package com.trustspace.mapper;

import com.trustspace.dto.response.ChatRoomResponse;
import com.trustspace.dto.response.MessageResponse;
import com.trustspace.entity.ChatRoom;
import com.trustspace.entity.Message;
import com.trustspace.repository.MessageRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PropertyMapper.class})
public abstract class ChatMapper {
    
    @Autowired
    protected MessageRepository messageRepository;
    
    @Autowired
    protected PropertyMapper propertyMapper;
    
    @Mapping(target = "property", expression = "java(chatRoom.getProperty() != null ? propertyMapper.toResponse(chatRoom.getProperty(), null) : null)")
    @Mapping(target = "lastMessage", expression = "java(getLastMessage(chatRoom))")
    @Mapping(target = "unreadCount", expression = "java(getUnreadCount(chatRoom, userId))")
    public abstract ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom, Long userId);
    
    @Mapping(target = "chatRoomId", source = "chatRoom.id")
    public abstract MessageResponse toMessageResponse(Message message);
    
    protected MessageResponse getLastMessage(ChatRoom chatRoom) {
        if (chatRoom.getMessages() == null || chatRoom.getMessages().isEmpty()) {
            return null;
        }
        Message lastMessage = chatRoom.getMessages().get(chatRoom.getMessages().size() - 1);
        return toMessageResponse(lastMessage);
    }
    
    protected Long getUnreadCount(ChatRoom chatRoom, Long userId) {
        return chatRoom.getMessages().stream()
                .filter(m -> m.getReceiver().getId().equals(userId) && !m.getIsRead())
                .count();
    }
}
