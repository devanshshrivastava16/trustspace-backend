package com.trustspace.controller;

import com.trustspace.dto.request.BookingRequest;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.BookingResponse;
import com.trustspace.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }
    
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }
    
    @GetMapping("/property/{propertyId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<BookingResponse>> getPropertyBookings(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookingService.getPropertyBookings(propertyId));
    }
    
    @GetMapping("/owner")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<BookingResponse>> getOwnerBookings() {
        return ResponseEntity.ok(bookingService.getOwnerBookings());
    }
    
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }
    
    @PutMapping("/{bookingId}/approve")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<BookingResponse> approveBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.approveBooking(bookingId));
    }
    
    @PutMapping("/{bookingId}/reject")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<BookingResponse> rejectBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.rejectBooking(bookingId));
    }
    
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, reason));
    }
}
