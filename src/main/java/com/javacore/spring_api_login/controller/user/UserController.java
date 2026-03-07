package com.javacore.spring_api_login.controller.user;

import com.javacore.spring_api_login.dtos.Request.RegisterUserUpdateRequest;
import com.javacore.spring_api_login.dtos.Response.RegisterUserResponse;
import com.javacore.spring_api_login.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#publicId)")
    @GetMapping("/{publicId}")
    public ResponseEntity<RegisterUserResponse> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(userService.findByPublicId(publicId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RegisterUserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{publicId}")
    public ResponseEntity<RegisterUserResponse> update(
            @PathVariable UUID publicId,
            @RequestBody @Valid RegisterUserUpdateRequest updateRequest) {
        return ResponseEntity.ok(userService.update(publicId, updateRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{publicId}")
    public void delete(@PathVariable UUID publicId) {
        userService.delete(publicId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{publicId}/restore")
    public ResponseEntity<RegisterUserResponse> restore(@PathVariable UUID publicId) {
        return ResponseEntity.ok(userService.restore(publicId));
    }
}