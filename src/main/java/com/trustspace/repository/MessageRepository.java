package com.trustspace.repository;

import com.trustspace.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomIdOrderBySentAtAsc(Long chatRoomId);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :userId AND m.isRead = false")
    Long countUnreadMessagesByUserId(@Param("userId") Long userId);
}
