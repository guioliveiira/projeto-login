package com.javacore.spring_api_login.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenConfiguration tokenConfiguration;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            tokenConfiguration.validateToken(token)
                    .ifPresent(data -> {
                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            var authorities = data.role()
                                    .getAuthorities()
                                    .stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList();

                            var authentication = new UsernamePasswordAuthenticationToken(
                                    data.email(),
                                    null,
                                    authorities
                            );

                            SecurityContextHolder
                                    .getContext()
                                    .setAuthentication(authentication);
                        }
                    });
        }
        filterChain.doFilter(request, response);
    }
}
