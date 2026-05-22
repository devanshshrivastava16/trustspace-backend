package com.trustspace.controller;

import com.trustspace.dto.request.ReviewRequest;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.ReviewResponse;
import com.trustspace.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request));
    }
    
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReviewResponse>> getPropertyReviews(@PathVariable Long propertyId) {
        return ResponseEntity.ok(reviewService.getPropertyReviews(propertyId));
    }
    
    @GetMapping("/property/{propertyId}/paginated")
    public ResponseEntity<Page<ReviewResponse>> getPropertyReviewsPaginated(
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getPropertyReviewsPaginated(propertyId, page, size));
    }
    
    @GetMapping("/my-reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {
        return ResponseEntity.ok(reviewService.getMyReviews());
    }
    
    @PutMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, request));
    }
    
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }
    
    @GetMapping("/property/{propertyId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long propertyId) {
        return ResponseEntity.ok(reviewService.getAverageRating(propertyId));
    }
}
