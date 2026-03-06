package com.javacore.spring_api_login.service.auth;

import com.javacore.spring_api_login.config.TokenConfiguration;
import com.javacore.spring_api_login.domain.email.EmailNormalizer;
import com.javacore.spring_api_login.domain.name.NameNormalizer;
import com.javacore.spring_api_login.dtos.Request.LoginUserRequest;
import com.javacore.spring_api_login.dtos.Request.RegisterUserRequest;
import com.javacore.spring_api_login.dtos.Response.LoginUserResponse;
import com.javacore.spring_api_login.dtos.Response.RegisterUserResponse;
import com.javacore.spring_api_login.entity.User;
import com.javacore.spring_api_login.entity.UserRole;
import com.javacore.spring_api_login.exception.custom.BusinessException;
import com.javacore.spring_api_login.exception.custom.InvalidCredentialsException;
import com.javacore.spring_api_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfiguration tokenConfiguration;

    @Override
    public LoginUserResponse login(LoginUserRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmailAndDeletedFalse(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou senha incorretos"));

        String token = tokenConfiguration.generateToken(user);

        return new LoginUserResponse(token);
    }

    @Override
    public RegisterUserResponse register(RegisterUserRequest request) {
        String normalizerName = NameNormalizer.normalizer(request.name());
        String normalizerEmail = EmailNormalizer.normalize(request.email());

        if (userRepository.existsByEmailAndDeletedFalse(normalizerEmail)) {
            throw new BusinessException("Email já foi cadastrado");
        }

        User user = User.builder()
                .name(normalizerName)
                .email(normalizerEmail)
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterUserResponse(
                savedUser.getPublicId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt(),
                savedUser.getDeleted()
        );
    }
}
