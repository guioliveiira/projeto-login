package com.javacore.spring_api_login.dtos.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotEmpty(message = "Nome é obrigatório")
        @Size(min = 2, message = "Nome deve conter ao menos 2 caracteres")
        @Pattern(regexp = "^[\\p{L} ]+$", message = "Nome inválido")
        String name,

        @NotEmpty(message = "Email é obrigatória")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
                message = "Email inválido")
        String email,

        @NotEmpty(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve conter ao menos 8 caracteres")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
                message = "Senha inválida")
        String password,

        @NotEmpty(message = "Confirmar senha é obrigatório")
        String confirmPassword
) {
}
