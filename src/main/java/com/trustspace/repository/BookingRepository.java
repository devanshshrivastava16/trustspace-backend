package com.trustspace.repository;

import com.trustspace.entity.Booking;
import com.trustspace.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByPropertyId(Long propertyId);
    List<Booking> findByStatus(BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.property.owner.id = :ownerId")
    List<Booking> findByOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b " +
           "WHERE b.property.id = :propertyId AND b.bookingDate = :date " +
           "AND b.status IN ('PENDING', 'APPROVED') " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    Boolean existsOverlappingBooking(
        @Param("propertyId") Long propertyId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
}
