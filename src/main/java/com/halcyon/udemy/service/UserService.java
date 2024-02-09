package com.halcyon.udemy.service;

import com.halcyon.udemy.model.User;
import com.halcyon.udemy.repository.UserRepository;
import com.halcyon.udemy.security.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found."));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found."));
    }

    private User findCurrentUser() {
        JwtAuthentication jwtAuth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return findByEmail(jwtAuth.getEmail());
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User topUpBalance(BigDecimal amount) {
        User user = findCurrentUser();
        user.setBalance(user.getBalance().add(amount));

        return create(user);
    }
}
