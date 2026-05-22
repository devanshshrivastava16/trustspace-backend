package com.trustspace.entity;

import com.trustspace.enums.BookingStatus;
import com.trustspace.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    
    @Column(nullable = false)
    private LocalDate bookingDate;
    
    @Column(nullable = false)
    private LocalTime startTime;
    
    @Column(nullable = false)
    private LocalTime endTime;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    private String paymentId;
    
    @Column(length = 1000)
    private String specialRequests;
    
    private Integer numberOfGuests;
    
    private String cancellationReason;
    
    private LocalDateTime cancelledAt;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
