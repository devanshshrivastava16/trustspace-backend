package com.trustspace.service;

import com.trustspace.dto.response.AmenityResponse;
import com.trustspace.mapper.AmenityMapper;
import com.trustspace.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AmenityService {
    
    private final AmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;
    
    public List<AmenityResponse> getAllAmenities() {
        return amenityRepository.findAll().stream()
                .map(amenityMapper::toResponse)
                .toList();
    }
}
