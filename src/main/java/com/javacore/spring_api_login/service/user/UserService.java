package com.javacore.spring_api_login.service.user;

import com.javacore.spring_api_login.dtos.Request.RegisterUserUpdateRequest;
import com.javacore.spring_api_login.dtos.Response.RegisterUserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisterUserResponse findByPublicId(UUID publicId);

    List<RegisterUserResponse> findAll();

    RegisterUserResponse update(UUID publicId, RegisterUserUpdateRequest updateRequest);

    void delete(UUID publicId);

    RegisterUserResponse restore(UUID publicId);
}
