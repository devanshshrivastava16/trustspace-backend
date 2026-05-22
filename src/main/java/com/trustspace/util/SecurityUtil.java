package com.trustspace.util;

import com.trustspace.entity.User;
import com.trustspace.exception.UnauthorizedException;
import com.trustspace.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository repository;

    private static UserRepository userRepository;

    @PostConstruct
    public void init() {
        userRepository = repository;
    }

    public static User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {

            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UnauthorizedException("User not found"));
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentUserEmail() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        return authentication.getName();
    }
}