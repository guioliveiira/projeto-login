package com.javacore.spring_api_login.config;

import com.javacore.spring_api_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public boolean isOwner(UUID publicId) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String email = authentication.getName();

        return userRepository.findByEmailAndDeletedFalse(email)
                .map(user -> user.getPublicId().equals(publicId))
                .orElse(false);
    }
}
