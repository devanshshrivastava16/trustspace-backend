package com.trustspace.service;

import com.trustspace.dto.response.CityResponse;
import com.trustspace.entity.City;
import com.trustspace.exception.ResourceNotFoundException;
import com.trustspace.mapper.CityMapper;
import com.trustspace.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CityService {
    
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    
    public List<CityResponse> getAllCities() {
        return cityRepository.findAll().stream()
                .map(cityMapper::toResponse)
                .toList();
    }
    
    public List<CityResponse> getTrendingCities() {
        return cityRepository.findByIsTrending(true).stream()
                .map(cityMapper::toResponse)
                .toList();
    }
    
    public CityResponse getCityById(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
        
        return cityMapper.toResponse(city);
    }
    
    public CityResponse getCityByName(String name) {
        City city = cityRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("City not found: " + name));
        
        return cityMapper.toResponse(city);
    }
}
