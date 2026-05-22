package com.trustspace.service;

import com.trustspace.dto.request.PropertyRequest;
import com.trustspace.dto.response.ApiResponse;
import com.trustspace.dto.response.PropertyResponse;
import com.trustspace.entity.*;
import com.trustspace.enums.PropertyStatus;
import com.trustspace.enums.PropertyType;
import com.trustspace.enums.Role;
import com.trustspace.exception.BadRequestException;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.exception.UnauthorizedException;
import com.trustspace.mapper.PropertyMapper;
import com.trustspace.repository.*;
import com.trustspace.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PropertyService {
    
    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final AmenityRepository amenityRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final PropertyAmenityRepository propertyAmenityRepository;
    private final WishlistRepository wishlistRepository;
    private final CloudinaryService cloudinaryService;
    private final PropertyMapper propertyMapper;
    
    public PropertyResponse createProperty(PropertyRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        // Update user role to OWNER if not already
        if (currentUser.getRole() == Role.USER) {
            currentUser.setRole(Role.OWNER);
        }
        
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
        
        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .propertyType(request.getPropertyType())
                .city(city)
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .hourlyPrice(request.getHourlyPrice())
                .dailyPrice(request.getDailyPrice())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .parkingAvailable(request.getParkingAvailable())
                .furnished(request.getFurnished())
                .guestCapacity(request.getGuestCapacity())
                .size(request.getSize())
                .status(PropertyStatus.ACTIVE)
                .owner(currentUser)
                .build();
        
        property = propertyRepository.save(property);
        
        // Add amenities if provided
        if (request.getAmenityIds() != null && !request.getAmenityIds().isEmpty()) {
            for (Long amenityId : request.getAmenityIds()) {
                Amenity amenity = amenityRepository.findById(amenityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Amenity not found: " + amenityId));
                
                PropertyAmenity propertyAmenity = PropertyAmenity.builder()
                        .property(property)
                        .amenity(amenity)
                        .build();
                
                propertyAmenityRepository.save(propertyAmenity);
            }
        }
        
        return propertyMapper.toResponse(property, currentUser.getId());
    }
    
    public PropertyResponse updateProperty(Long propertyId, PropertyRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        // Check ownership
        if (!property.getOwner().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("You don't have permission to update this property");
        }
        
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPropertyType(request.getPropertyType());
        property.setAddress(request.getAddress());
        property.setLatitude(request.getLatitude());
        property.setLongitude(request.getLongitude());
        property.setHourlyPrice(request.getHourlyPrice());
        property.setDailyPrice(request.getDailyPrice());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setParkingAvailable(request.getParkingAvailable());
        property.setFurnished(request.getFurnished());
        property.setGuestCapacity(request.getGuestCapacity());
        property.setSize(request.getSize());
        
        if (request.getCityId() != null) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("City not found"));
            property.setCity(city);
        }
        
        // Update amenities
        if (request.getAmenityIds() != null) {
            propertyAmenityRepository.deleteByPropertyId(propertyId);
            
            for (Long amenityId : request.getAmenityIds()) {
                Amenity amenity = amenityRepository.findById(amenityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));
                
                PropertyAmenity propertyAmenity = PropertyAmenity.builder()
                        .property(property)
                        .amenity(amenity)
                        .build();
                
                propertyAmenityRepository.save(propertyAmenity);
            }
        }
        
        property = propertyRepository.save(property);
        return propertyMapper.toResponse(property, currentUser.getId());
    }
    
    public PropertyResponse getPropertyById(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        Long currentUserId = null;
        try {
            currentUserId = SecurityUtil.getCurrentUserId();
        } catch (Exception e) {
            // User not authenticated, continue without user context
        }
        
        return propertyMapper.toResponse(property, currentUserId);
    }
    
    public Page<PropertyResponse> getPropertiesByCity(
            Long cityId,
            PropertyType propertyType,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minCapacity,
            Boolean parkingAvailable,
            Boolean furnished,
            String sortBy,
            int page,
            int size
    ) {
        Pageable pageable = createPageable(page, size, sortBy);
        
        Page<Property> properties = propertyRepository.findWithFilters(
                cityId,
                PropertyStatus.ACTIVE,
                propertyType,
                minPrice,
                maxPrice,
                minCapacity,
                pageable
        );
        
        Long currentUserId = null;
        try {
            currentUserId = SecurityUtil.getCurrentUserId();
        } catch (Exception e) {
            // User not authenticated
        }
        
        final Long userId = currentUserId;
        return properties.map(property -> propertyMapper.toResponse(property, userId));
    }
    
    public List<PropertyResponse> getMyProperties() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<Property> properties = propertyRepository.findByOwnerId(currentUser.getId());
        
        return properties.stream()
                .map(property -> propertyMapper.toResponse(property, currentUser.getId()))
                .toList();
    }
    
    public PropertyResponse uploadPropertyImages(Long propertyId, List<MultipartFile> files) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        // Check ownership
        if (!property.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't have permission to upload images for this property");
        }
        
        int currentMaxOrder = propertyImageRepository.findByPropertyIdOrderByDisplayOrderAsc(propertyId)
                .stream()
                .mapToInt(PropertyImage::getDisplayOrder)
                .max()
                .orElse(-1);
        
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            Map<String, String> uploadResult = cloudinaryService.uploadImage(file, "properties");
            
            PropertyImage propertyImage = PropertyImage.builder()
                    .property(property)
                    .imageUrl(uploadResult.get("imageUrl"))
                    .publicId(uploadResult.get("publicId"))
                    .isPrimary(currentMaxOrder == -1 && i == 0)
                    .displayOrder(currentMaxOrder + i + 1)
                    .build();
            
            propertyImageRepository.save(propertyImage);
        }
        
        return propertyMapper.toResponse(property, currentUser.getId());
    }
    
    public ApiResponse deletePropertyImage(Long propertyId, Long imageId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        if (!property.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't have permission to delete images for this property");
        }
        
        PropertyImage image = propertyImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        
        if (!image.getProperty().getId().equals(propertyId)) {
            throw new BadRequestException("Image does not belong to this property");
        }
        
        cloudinaryService.deleteImage(image.getPublicId());
        propertyImageRepository.delete(image);
        
        return ApiResponse.builder()
                .success(true)
                .message("Image deleted successfully")
                .build();
    }
    
    public ApiResponse deleteProperty(Long propertyId) {
        User currentUser = SecurityUtil.getCurrentUser();
        
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        if (!property.getOwner().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("You don't have permission to delete this property");
        }
        
        // Delete all images from Cloudinary
        List<PropertyImage> images = propertyImageRepository.findByPropertyIdOrderByDisplayOrderAsc(propertyId);
        for (PropertyImage image : images) {
            try {
                cloudinaryService.deleteImage(image.getPublicId());
            } catch (Exception e) {
                log.warn("Failed to delete image: {}", image.getPublicId(), e);
            }
        }
        
        propertyRepository.delete(property);
        
        return ApiResponse.builder()
                .success(true)
                .message("Property deleted successfully")
                .build();
    }
    
    public Page<PropertyResponse> searchProperties(String keyword, Long cityId, int page, int size) {
        // Implement search logic here
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Property> properties = propertyRepository.findByCityIdAndStatus(cityId, PropertyStatus.ACTIVE, pageable);
        
        Long currentUserId = null;
        try {
            currentUserId = SecurityUtil.getCurrentUserId();
        } catch (Exception e) {
            // User not authenticated
        }
        
        final Long userId = currentUserId;
        return properties.map(property -> propertyMapper.toResponse(property, userId));
    }
    
    private Pageable createPageable(int page, int size, String sortBy) {
        Sort sort = switch (sortBy != null ? sortBy : "newest") {
            case "price_low" -> Sort.by("hourlyPrice").ascending();
            case "price_high" -> Sort.by("hourlyPrice").descending();
            case "rating" -> Sort.by("createdAt").descending(); // Would need custom query for rating
            default -> Sort.by("createdAt").descending();
        };
        
        return PageRequest.of(page, size, sort);
    }
}
