package com.trustspace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "amenities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amenity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    private String icon; // Icon name or URL
    
    @Column(length = 500)
    private String description;
    
    @OneToMany(mappedBy = "amenity", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PropertyAmenity> propertyAmenities = new ArrayList<>();
}
