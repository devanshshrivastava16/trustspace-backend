package com.trustspace.repository;

import com.trustspace.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndPropertyId(Long userId, Long propertyId);
    Boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);
    void deleteByUserIdAndPropertyId(Long userId, Long propertyId);
}
