package com.javacore.spring_api_login.dtos.Response;

import com.javacore.spring_api_login.entity.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterUserResponse(
        UUID publicId,
        String name,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean deleted
) {
}
