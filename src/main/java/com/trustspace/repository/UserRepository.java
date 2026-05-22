package com.trustspace.repository;

import com.trustspace.entity.User;
import com.trustspace.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByResetPasswordToken(String token);
    List<User> findByRole(Role role);
    List<User> findByCityId(Long cityId);
}
