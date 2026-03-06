package com.javacore.spring_api_login.dtos.Response;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterUserResponse(
        UUID publicId,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean deleted
) {
}
