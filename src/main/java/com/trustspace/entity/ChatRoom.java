package com.trustspace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id")
    private Property property;
    
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sentAt ASC")
    @Builder.Default
    private List<Message> messages = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
