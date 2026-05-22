package com.trustspace.repository;

import com.trustspace.entity.Property;
import com.trustspace.enums.PropertyStatus;
import com.trustspace.enums.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByCityId(Long cityId);
    List<Property> findByOwnerId(Long ownerId);
    List<Property> findByStatus(PropertyStatus status);
    
    Page<Property> findByCityIdAndStatus(Long cityId, PropertyStatus status, Pageable pageable);
    
    @Query("SELECT p FROM Property p WHERE p.city.id = :cityId AND p.status = :status " +
           "AND (:propertyType IS NULL OR p.propertyType = :propertyType) " +
           "AND (:minPrice IS NULL OR p.hourlyPrice >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.hourlyPrice <= :maxPrice) " +
           "AND (:minCapacity IS NULL OR p.guestCapacity >= :minCapacity)")
    Page<Property> findWithFilters(
        @Param("cityId") Long cityId,
        @Param("status") PropertyStatus status,
        @Param("propertyType") PropertyType propertyType,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("minCapacity") Integer minCapacity,
        Pageable pageable
    );
    
    @Query("SELECT p FROM Property p WHERE p.city.id = :cityId AND p.status = 'ACTIVE' " +
           "ORDER BY (SELECT AVG(r.rating) FROM Review r WHERE r.property = p) DESC")
    Page<Property> findTopRatedByCityId(@Param("cityId") Long cityId, Pageable pageable);
}
