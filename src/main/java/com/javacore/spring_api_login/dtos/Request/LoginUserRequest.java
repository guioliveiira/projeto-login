package com.javacore.spring_api_login.dtos.Request;

import jakarta.validation.constraints.NotEmpty;

public record LoginUserRequest(
        @NotEmpty(message = "Email é obrigatório")
        String email,
        @NotEmpty(message = "Senha é obrigatória")
        String password
) {
}
