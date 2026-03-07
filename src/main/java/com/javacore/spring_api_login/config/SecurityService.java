package com.javacore.spring_api_login.config;

import com.javacore.spring_api_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public boolean isOwner(UUID publicId) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("Tentativa de verificação de ownership sem autenticação publicId={}", publicId);
            return false;
        }

        String email = authentication.getName();

        log.debug("Verificando ownership userEmail={} publicId={}", email, publicId);

        return userRepository.findByEmailAndDeletedFalse(email)
                .map(user -> user.getPublicId().equals(publicId))
                .orElseGet(() -> {
                    log.warn("Usuário autenticado não encontrado no banco email={}", email);
                    return false;
                });
    }
}
