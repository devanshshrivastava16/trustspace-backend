package com.trustspace.service;

import com.trustspace.dto.request.ReviewRequest;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.ReviewResponse;
import com.trustspace.entity.Booking;
import com.trustspace.entity.Property;
import com.trustspace.entity.Review;
import com.trustspace.entity.User;
import com.trustspace.enums.BookingStatus;
import com.trustspace.exception.BadRequestException;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.exception.UnauthorizedException;
import com.trustspace.mapper.ReviewMapper;
import com.trustspace.repository.BookingRepository;
import com.trustspace.repository.PropertyRepository;
import com.trustspace.repository.ReviewRepository;
import com.trustspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;
    private final ReviewMapper reviewMapper;
    
    public ReviewResponse createReview(ReviewRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        // Check if user already reviewed this property
        if (reviewRepository.existsByUserIdAndPropertyId(currentUser.getId(), request.getPropertyId())) {
            throw new BadRequestException("You have already reviewed this property");
        }
        
        // If booking ID provided, verify booking
        Booking booking = null;
        if (request.getBookingId() != null) {
            booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
            
            if (!booking.getUser().getId().equals(currentUser.getId())) {
                throw new UnauthorizedException("This is not your booking");
            }
            
            if (booking.getStatus() != BookingStatus.COMPLETED) {
                throw new BadRequestException("You can only review completed bookings");
            }
        }
        
        Review review = Review.builder()
                .user(currentUser)
                .property(property)
                .booking(booking)
                .rating(request.getRating())
                .comment(request.getComment())
                .images(request.getImages())
                .build();
        
        review = reviewRepository.save(review);
        
        return reviewMapper.toResponse(review);
    }
    
    public List<ReviewResponse> getPropertyReviews(Long propertyId) {
        List<Review> reviews = reviewRepository.findByPropertyId(propertyId);
        
        return reviews.stream()
                .map(reviewMapper::toResponse)
                .toList();
    }
    
    public Page<ReviewResponse> getPropertyReviewsPaginated(Long propertyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviews = reviewRepository.findByPropertyId(propertyId, pageable);
        
        return reviews.map(reviewMapper::toResponse);
    }
    
    public List<ReviewResponse> getMyReviews() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<Review> reviews = reviewRepository.findByUserId(currentUser.getId());
        
        return reviews.stream()
                .map(reviewMapper::toResponse)
                .toList();
    }
    
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only update your own reviews");
        }
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setImages(request.getImages());
        
        review = reviewRepository.save(review);
        
        return reviewMapper.toResponse(review);
    }
    
    public ApiResponse deleteReview(Long reviewId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only delete your own reviews");
        }
        
        reviewRepository.delete(review);
        
        return ApiResponse.builder()
                .success(true)
                .message("Review deleted successfully")
                .build();
    }
    
    public Double getAverageRating(Long propertyId) {
        Double average = reviewRepository.getAverageRatingByPropertyId(propertyId);
        return average != null ? average : 0.0;
    }
    
    public Long getTotalReviews(Long propertyId) {
        return reviewRepository.countByPropertyId(propertyId);
    }
}
