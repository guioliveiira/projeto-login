package com.javacore.spring_api_login.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Builder
@Getter
public class ApiErrorResponse {
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);
    private Integer status;
    private String error;
    private String message;

    @Builder.Default
    private List<String> details = List.of();
    private String path;
}
