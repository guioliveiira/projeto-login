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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public RegisterUserResponse findByPublicId(UUID publicId) {
        log.info("Buscando usuário com id={}", publicId);

        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado com id={}", publicId);
                    return new ResourceNotFoundException("Usuário não foi encontrado");
                });

        return toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegisterUserResponse> findAll() {
        log.debug("Listando todos os usuários ativos");

        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getDeleted())
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RegisterUserResponse update(UUID publicId, RegisterUserUpdateRequest updateRequest) {

        log.info("Iniciando atualização do usuário publicId={}", publicId);

       User user = findActiveUserOrThrow(publicId);

        if (updateRequest.name() != null) {
            log.debug("Atualizando nome do usuário publicId={}", publicId);
            user.setName(NameNormalizer.normalizer(updateRequest.name()));
        }

        if (updateRequest.email() != null) {
            String normalizerEmail = EmailNormalizer.normalize(updateRequest.email());

            if (!normalizerEmail.equals(user.getEmail()) &&
                    userRepository.existsByEmailAndDeletedFalse(normalizerEmail)) {
                log.warn("Falha ao atualizar email - email já cadastrado publicId={} email={}",
                        publicId, normalizerEmail);
                throw new BusinessException("Email já foi cadastrado");
            }
            log.debug("Atualizando email do usuário publicId={}", publicId);
            user.setEmail(normalizerEmail);
        }

        if (updateRequest.password() != null) {
            if (!updateRequest.password().equals(updateRequest.confirmPassword())) {
                log.warn("Falha ao atualizar senha - senhas não coincidem publicId={}", publicId);
                throw new BusinessException("As senhas não coincidem");
            }
            log.debug("Atualizando senha do usuário publicId={}", publicId);
            user.setPassword(passwordEncoder.encode(updateRequest.password()));
        }

        log.info("Usuário atualizado com sucesso publicId={}", publicId);
        return toResponse(user);
    }

    @Override
    public void delete(UUID publicId) {

        log.info("Solicitação para desativar usuário publicId={}", publicId);

        User user = findActiveUserOrThrow(publicId);

        if (user.getDeleted()) {
            log.warn("Tentativa de desativar usuário já desativado publicId={}", publicId);
            throw new BusinessException("Usuário já está desativado");
        }

        user.setDeleted(true);
        log.info("Usuário desativado com sucesso publicId={}", publicId);
    }

    @Override
    public RegisterUserResponse restore(UUID publicId) {

        log.info("Solicitação para restaurar usuário publicId={}", publicId);

        User user = findActiveUserOrThrow(publicId);

        if (userRepository.existsByEmailAndDeletedFalse(user.getEmail())) {
            log.warn("Falha ao restaurar usuário - email já está em uso publicId={} email={}",
                    publicId, user.getEmail());
            throw new BusinessException("Email já foi cadastrado");
        }

        if (!user.getDeleted()) {
            log.warn("Tentativa de restaurar usuário que já está ativo publicId={}", publicId);
            throw new BusinessException("Usuário já está ativado");
        }

        log.info("Usuário restaurado com sucesso publicId={}", publicId);
        user.setDeleted(false);
        user.setDeteledAt(null);
        return toResponse(user);
    }

    @Override
    public RegisterUserResponse promoteToAdmin(UUID publicId) {

        log.info("Solicitação para promover usuário para ADMIN publicId={}", publicId);

        User user = findActiveUserOrThrow(publicId);

        log.debug("Promovendo usuário id={}", publicId);

        if (user.getRole().equals(UserRole.ADMIN)) {
            log.warn("Tentativa de promover usuário que já é ADMIN publicId={}", publicId);
            throw new BusinessException("Usuário já é admin");
        }

        log.info("Usuário promovido para ADMIN publicId={}", publicId);
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

    private User findActiveUserOrThrow(UUID publicId) {
        return userRepository.findByPublicIdAndDeletedFalse(publicId)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado com id={}", publicId);
                    return new ResourceNotFoundException("Usuário não foi encontrado");
                });
    }
}
