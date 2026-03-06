package com.javacore.spring_api_login.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    USER(List.of("ROLE_USER")),
    ADMIN(List.of("ROLE_ADMIN", "ROLE_USER"));

    private final List<String> authorities;

}
