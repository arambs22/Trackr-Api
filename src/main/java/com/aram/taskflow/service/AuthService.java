package com.aram.taskflow.service;

import com.aram.taskflow.domain.User;
import com.aram.taskflow.dto.AuthResponse;
import com.aram.taskflow.dto.LoginRequest;
import com.aram.taskflow.dto.RegisterRequest;
import com.aram.taskflow.repository.UserRepository;
import com.aram.taskflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {
        String email = req.email().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email ya está registrado");
        }

        User user = User.builder()
                .name(req.name())
                .email(email)
                .passwordHash(passwordEncoder.encode(req.password()))
                .build();

        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }

    public AuthResponse login(LoginRequest req) {
        String email = req.email().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }
}