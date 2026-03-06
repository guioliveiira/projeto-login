package com.javacore.spring_api_login.controller.auth;

import com.javacore.spring_api_login.dtos.Request.LoginUserRequest;
import com.javacore.spring_api_login.dtos.Request.RegisterUserRequest;
import com.javacore.spring_api_login.dtos.Response.LoginUserResponse;
import com.javacore.spring_api_login.dtos.Response.RegisterUserResponse;
import com.javacore.spring_api_login.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}