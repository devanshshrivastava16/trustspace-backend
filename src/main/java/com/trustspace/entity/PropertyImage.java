package com.trustspace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "property_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    
    @Column(nullable = false)
    private String imageUrl;
    
    @Column(nullable = false)
    private String publicId;
    
    @Builder.Default
    private Boolean isPrimary = false;
    
    @Builder.Default
    private Integer displayOrder = 0;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;
}
