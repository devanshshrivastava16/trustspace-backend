package com.trustspace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_amenities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyAmenity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;
    
    private Boolean isAvailable = true;
}
