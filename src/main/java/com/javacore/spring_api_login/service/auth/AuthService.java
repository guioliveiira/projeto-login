package com.javacore.spring_api_login.service.auth;

import com.javacore.spring_api_login.dtos.Request.LoginUserRequest;
import com.javacore.spring_api_login.dtos.Request.RegisterUserRequest;
import com.javacore.spring_api_login.dtos.Response.LoginUserResponse;
import com.javacore.spring_api_login.dtos.Response.RegisterUserResponse;

public interface AuthService {

    LoginUserResponse login(LoginUserRequest request);

    RegisterUserResponse register(RegisterUserRequest request);
}
