package com.trustspace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishlists", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "property_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt;
}
