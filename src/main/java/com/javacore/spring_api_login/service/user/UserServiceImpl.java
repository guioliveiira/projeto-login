package com.javacore.spring_api_login.service.user;

import com.javacore.spring_api_login.domain.email.EmailNormalizer;
import com.javacore.spring_api_login.domain.name.NameNormalizer;
import com.javacore.spring_api_login.dtos.Request.RegisterUserUpdateRequest;
import com.javacore.spring_api_login.dtos.Response.RegisterUserResponse;
import com.javacore.spring_api_login.entity.User;
import com.javacore.spring_api_login.entity.UserRole;
import com.javacore.spring_api_login.exception.custom.BusinessException;
import com.javacore.spring_api_login.exception.custom.ResourceNotFoundException;
import com.javacore.spring_api_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public RegisterUserResponse findByPublicId(UUID publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não foi encontrado"));

        return toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegisterUserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getDeleted())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RegisterUserResponse update(UUID publicId, RegisterUserUpdateRequest updateRequest) {
        User user = userRepository.findByPublicIdAndDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (updateRequest.name() != null) {
            user.setName(NameNormalizer.normalizer(updateRequest.name()));
        }

        if (updateRequest.email() != null) {
            String normalizerEmail = EmailNormalizer.normalize(updateRequest.email());

            if (!normalizerEmail.equals(user.getEmail()) &&
                    userRepository.existsByEmailAndDeletedFalse(normalizerEmail)) {
                throw new BusinessException("Email já foi cadastrado");
            }
            user.setEmail(normalizerEmail);
        }

        if (updateRequest.password() != null) {
            if (!updateRequest.password().equals(updateRequest.confirmPassword())) {
                throw new BusinessException("As senhas não coincidem");
            }
            user.setPassword(passwordEncoder.encode(updateRequest.password()));
        }

        return toResponse(user);
    }

    @Override
    public void delete(UUID publicId) {
        User user = userRepository.findByPublicIdAndDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não foi encontrado"));

        if (user.getDeleted()) {
            throw new BusinessException("Usuário já está desativado");
        }

        user.setDeleted(true);
    }

    @Override
    public RegisterUserResponse restore(UUID publicId) {
        User user = userRepository.findByPublicIdAndDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não foi encontrado"));

        if (userRepository.existsByEmailAndDeletedFalse(user.getEmail())) {
            throw new BusinessException("Email já foi cadastrado");
        }

        if (!user.getDeleted()) {
            throw new BusinessException("Usuário já está ativado");
        }

        user.setDeleted(false);
        user.setDeteledAt(null);
        return toResponse(user);
    }

    @Override
    public RegisterUserResponse promoteToAdmin(UUID publicId) {
        User user = userRepository.findByPublicIdAndDeletedFalse(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não foi encontrado"));

        if (user.getRole().equals(UserRole.ADMIN)) {
            throw new BusinessException("Usuário já é admin");
        }

        user.setRole(UserRole.ADMIN);
        return toResponse(user);
    }

    private RegisterUserResponse toResponse(User user) {
        return new RegisterUserResponse(
                user.getPublicId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeleted()
        );
    }
}
