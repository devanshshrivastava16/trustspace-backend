package com.trustspace.entity;

import com.trustspace.enums.PropertyStatus;
import com.trustspace.enums.PropertyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    
    @Column(nullable = false)
    private String address;
    
    private Double latitude;
    
    private Double longitude;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyPrice;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal dailyPrice;
    
    private Integer bedrooms;
    
    private Integer bathrooms;
    
    @Builder.Default
    private Boolean parkingAvailable = false;
    
    @Builder.Default
    private Boolean furnished = false;
    
    @Column(nullable = false)
    private Integer guestCapacity;
    
    private Double size; // in square feet
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PropertyStatus status = PropertyStatus.ACTIVE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PropertyImage> images = new ArrayList<>();
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PropertyAmenity> propertyAmenities = new ArrayList<>();
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Wishlist> wishlists = new ArrayList<>();
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Helper method to calculate average rating
    @Transient
    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
    
    // Helper method to get total reviews count
    @Transient
    public Integer getTotalReviews() {
        return reviews != null ? reviews.size() : 0;
    }
}
