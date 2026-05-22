package com.trustspace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private String state;
    
    @Column(nullable = false)
    private String country;
    
    private String imageUrl;
    
    @Builder.Default
    private Boolean isTrending = false;
    
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Property> properties = new ArrayList<>();
    
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    @Builder.Default
    private List<User> users = new ArrayList<>();
}
