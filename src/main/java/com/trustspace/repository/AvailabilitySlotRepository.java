package com.trustspace.repository;

import com.trustspace.entity.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByPropertyIdAndDate(Long propertyId, LocalDate date);
    List<AvailabilitySlot> findByPropertyIdAndDateBetween(Long propertyId, LocalDate startDate, LocalDate endDate);
}
