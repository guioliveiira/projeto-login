package com.javacore.spring_api_login.controller.user;

import com.javacore.spring_api_login.dtos.Request.RegisterUserUpdateRequest;
import com.javacore.spring_api_login.dtos.Response.RegisterUserResponse;
import com.javacore.spring_api_login.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{publicId}")
    public ResponseEntity<RegisterUserResponse> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(userService.findByPublicId(publicId));
    }

    @GetMapping
    public ResponseEntity<List<RegisterUserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<RegisterUserResponse> update(
            @PathVariable UUID publicId,
            @RequestBody @Valid RegisterUserUpdateRequest updateRequest) {
        return ResponseEntity.ok(userService.update(publicId, updateRequest));
    }

    public void delete(@PathVariable UUID publicId) {
        userService.delete(publicId);
    }

    @PatchMapping("/{publicId}/restore")
    public ResponseEntity<RegisterUserResponse> restore(@PathVariable UUID publicId) {
        return ResponseEntity.ok(userService.restore(publicId));
    }
}