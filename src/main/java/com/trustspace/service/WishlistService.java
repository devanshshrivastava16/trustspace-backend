package com.trustspace.service;

import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.PropertyResponse;
import com.trustspace.entity.Property;
import com.trustspace.entity.User;
import com.trustspace.entity.Wishlist;
import com.trustspace.exception.BadRequestException;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.mapper.PropertyMapper;
import com.trustspace.repository.PropertyRepository;
import com.trustspace.repository.WishlistRepository;
import com.trustspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WishlistService {
    
    private final WishlistRepository wishlistRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    
    public ApiResponse addToWishlist(Long propertyId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        // Check if already in wishlist
        if (wishlistRepository.existsByUserIdAndPropertyId(currentUser.getId(), propertyId)) {
            throw new BadRequestException("Property already in wishlist");
        }
        
        Wishlist wishlist = Wishlist.builder()
                .user(currentUser)
                .property(property)
                .build();
        
        wishlistRepository.save(wishlist);
        
        return ApiResponse.builder()
                .success(true)
                .message("Added to wishlist")
                .build();
    }
    
    public ApiResponse removeFromWishlist(Long propertyId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        if (!wishlistRepository.existsByUserIdAndPropertyId(currentUser.getId(), propertyId)) {
            throw new ResourceNotFoundException("Property not in wishlist");
        }
        
        wishlistRepository.deleteByUserIdAndPropertyId(currentUser.getId(), propertyId);
        
        return ApiResponse.builder()
                .success(true)
                .message("Removed from wishlist")
                .build();
    }
    
    public List<PropertyResponse> getMyWishlist() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<Wishlist> wishlists = wishlistRepository.findByUserId(currentUser.getId());
        
        return wishlists.stream()
                .map(wishlist -> propertyMapper.toResponse(wishlist.getProperty(), currentUser.getId()))
                .toList();
    }
    
    public Boolean isInWishlist(Long propertyId) {
        try {
            User currentUser = SecurityUtil.getCurrentUser();
            return wishlistRepository.existsByUserIdAndPropertyId(currentUser.getId(), propertyId);
        } catch (Exception e) {
            return false;
        }
    }
}
