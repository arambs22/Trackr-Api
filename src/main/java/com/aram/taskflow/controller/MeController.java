package com.aram.taskflow.controller;

import com.aram.taskflow.domain.User;
import com.aram.taskflow.dto.MeResponse;
import com.aram.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public MeResponse me(Authentication auth) {
        String email = (String) auth.getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow();
        return new MeResponse(user.getId(), user.getName(), user.getEmail());
    }
}