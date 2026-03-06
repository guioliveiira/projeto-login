package com.javacore.spring_api_login.dtos.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserUpdateRequest(
        @Size(min = 2, message = "Nome deve conter ao menos 2 caracteres")
        @Pattern(regexp = "^[\\p{L} ]+$", message = "Nome inválido")
        String name,

        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
                message = "Email inválido")
        String email,

        @Size(min = 8, message = "Senha deve conter ao menos 8 caracteres")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
                message = "Senha inválida")
        String password,

        String confirmPassword
) {
}
