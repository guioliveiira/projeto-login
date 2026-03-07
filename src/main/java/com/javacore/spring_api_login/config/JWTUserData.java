package com.javacore.spring_api_login.config;

import com.javacore.spring_api_login.entity.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record JWTUserData(
        String email,
        UUID publicId,
        UserRole role
) {
}
