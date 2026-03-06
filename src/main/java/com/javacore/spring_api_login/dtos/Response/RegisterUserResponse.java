package com.javacore.spring_api_login.dtos.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javacore.spring_api_login.entity.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterUserResponse(
        UUID publicId,
        String name,
        String email,
        UserRole role,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        LocalDateTime updatedAt,
        Boolean deleted
) {
}
