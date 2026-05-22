package com.trustspace.repository;

import com.trustspace.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);
    List<City> findByIsTrending(Boolean isTrending);
    List<City> findByState(String state);
}
