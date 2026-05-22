package com.trustspace.service;

import com.trustspace.dto.request.BookingRequest;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.BookingResponse;
import com.trustspace.entity.Booking;
import com.trustspace.entity.Property;
import com.trustspace.entity.User;
import com.trustspace.enums.BookingStatus;
import com.trustspace.enums.NotificationType;
import com.trustspace.enums.PaymentStatus;
import com.trustspace.exception.BadRequestException;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.exception.UnauthorizedException;
import com.trustspace.mapper.BookingMapper;
import com.trustspace.repository.BookingRepository;
import com.trustspace.repository.PropertyRepository;
import com.trustspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final BookingMapper bookingMapper;
    
    public BookingResponse createBooking(BookingRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        // Check if user is trying to book their own property
        if (property.getOwner().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot book your own property");
        }
        
        // Validate booking date and time
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new BadRequestException("End time must be after start time");
        }
        
        // Check for overlapping bookings
        Boolean hasOverlap = bookingRepository.existsOverlappingBooking(
                request.getPropertyId(),
                request.getBookingDate(),
                request.getStartTime(),
                request.getEndTime()
        );
        
        if (hasOverlap) {
            throw new BadRequestException("This time slot is already booked");
        }
        
        // Calculate total price
        BigDecimal totalPrice = calculateTotalPrice(
                property.getHourlyPrice(),
                request.getStartTime(),
                request.getEndTime()
        );
        
        Booking booking = Booking.builder()
                .user(currentUser)
                .property(property)
                .bookingDate(request.getBookingDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .totalPrice(totalPrice)
                .numberOfGuests(request.getNumberOfGuests())
                .specialRequests(request.getSpecialRequests())
                .status(BookingStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        
        booking = bookingRepository.save(booking);
        
        // Send notification to property owner
        notificationService.createNotification(
                property.getOwner().getId(),
                NotificationType.NEW_BOOKING,
                "New Booking Request",
                "You have a new booking request for " + property.getTitle(),
                "/bookings/" + booking.getId()
        );
        
        // Send email notification
        emailService.sendBookingNotification(
                property.getOwner().getEmail(),
                "You have a new booking request for your property: " + property.getTitle()
        );
        
        return bookingMapper.toResponse(booking);
    }
    
    public List<BookingResponse> getMyBookings() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<Booking> bookings = bookingRepository.findByUserId(currentUser.getId());
        
        return bookings.stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
    
    public List<BookingResponse> getPropertyBookings(Long propertyId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        if (!property.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't have permission to view bookings for this property");
        }
        
        List<Booking> bookings = bookingRepository.findByPropertyId(propertyId);
        
        return bookings.stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
    
    public List<BookingResponse> getOwnerBookings() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<Booking> bookings = bookingRepository.findByOwnerId(currentUser.getId());
        
        return bookings.stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
    
    public BookingResponse getBookingById(Long bookingId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        // Check if user has permission to view this booking
        if (!booking.getUser().getId().equals(currentUser.getId()) &&
            !booking.getProperty().getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't have permission to view this booking");
        }
        
        return bookingMapper.toResponse(booking);
    }
    
    public BookingResponse approveBooking(Long bookingId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        // Check if user is the property owner
        if (!booking.getProperty().getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only property owner can approve bookings");
        }
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only pending bookings can be approved");
        }
        
        booking.setStatus(BookingStatus.APPROVED);
        booking = bookingRepository.save(booking);
        
        // Send notification to user
        notificationService.createNotification(
                booking.getUser().getId(),
                NotificationType.BOOKING_APPROVED,
                "Booking Approved",
                "Your booking for " + booking.getProperty().getTitle() + " has been approved",
                "/bookings/" + booking.getId()
        );
        
        emailService.sendBookingNotification(
                booking.getUser().getEmail(),
                "Your booking has been approved for: " + booking.getProperty().getTitle()
        );
        
        return bookingMapper.toResponse(booking);
    }
    
    public BookingResponse rejectBooking(Long bookingId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        if (!booking.getProperty().getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only property owner can reject bookings");
        }
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only pending bookings can be rejected");
        }
        
        booking.setStatus(BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);
        
        // Send notification to user
        notificationService.createNotification(
                booking.getUser().getId(),
                NotificationType.BOOKING_REJECTED,
                "Booking Rejected",
                "Your booking for " + booking.getProperty().getTitle() + " has been rejected",
                "/bookings/" + booking.getId()
        );
        
        return bookingMapper.toResponse(booking);
    }
    
    public BookingResponse cancelBooking(Long bookingId, String reason) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        // Check if user owns this booking
        if (!booking.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only cancel your own bookings");
        }
        
        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot cancel completed or already cancelled bookings");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);
        
        // Notify property owner
        notificationService.createNotification(
                booking.getProperty().getOwner().getId(),
                NotificationType.BOOKING_CANCELLED,
                "Booking Cancelled",
                "A booking for " + booking.getProperty().getTitle() + " has been cancelled",
                "/bookings/" + booking.getId()
        );
        
        return bookingMapper.toResponse(booking);
    }
    
    private BigDecimal calculateTotalPrice(BigDecimal hourlyPrice, 
                                          java.time.LocalTime startTime, 
                                          java.time.LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long hours = duration.toHours();
        
        if (hours <= 0) {
            hours = 1; // Minimum 1 hour
        }
        
        return hourlyPrice.multiply(BigDecimal.valueOf(hours));
    }
}
