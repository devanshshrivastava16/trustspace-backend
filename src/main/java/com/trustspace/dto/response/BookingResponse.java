package com.trustspace.dto.response;

import com.trustspace.enums.BookingStatus;
import com.trustspace.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private UserResponse user;
    private PropertyResponse property;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private PaymentStatus paymentStatus;
    private String paymentId;
    private String specialRequests;
    private Integer numberOfGuests;
    private String cancellationReason;
    private LocalDateTime createdAt;
}
