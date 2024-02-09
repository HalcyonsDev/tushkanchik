package com.halcyon.udemy.service.auth;

import com.halcyon.udemy.dto.SignUpDto;
import com.halcyon.udemy.model.Token;
import com.halcyon.udemy.model.User;
import com.halcyon.udemy.payload.AuthRequest;
import com.halcyon.udemy.payload.AuthResponse;
import com.halcyon.udemy.security.JwtAuthentication;
import com.halcyon.udemy.service.TokenService;
import com.halcyon.udemy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse signup(SignUpDto dto) {
        if (userService.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists.");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .balance(BigDecimal.ZERO)
                .build();

        userService.create(user);

        String accessToken = jwtProvider.generateToken(user, false);
        String refreshToken = jwtProvider.generateToken(user, true);

        tokenService.create(new Token(refreshToken, user));

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(AuthRequest authRequest) {
        User user = userService.findByEmail(authRequest.getEmail());

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data.");
        }

        String accessToken = jwtProvider.generateToken(user, false);
        String refreshToken = jwtProvider.generateToken(user, true);

        tokenService.create(new Token(refreshToken, user));

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse getTokenByRefresh(String refreshToken, boolean isRefresh) {
        String subject = jwtProvider.extractRefreshClaims(refreshToken).getSubject();

        if (!jwtProvider.isValidRefreshToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is not valid.");
        }

        Token token = tokenService.findByValue(refreshToken);

        if (!token.getValue().equals(refreshToken) || !token.getOwner().getEmail().equals(subject)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data.");
        }

        User user = userService.findByEmail(subject);

        String accessToken = jwtProvider.generateToken(user, false);
        String newRefreshToken = null;

        if (isRefresh) {
            newRefreshToken = jwtProvider.generateToken(user ,true);
            tokenService.create(new Token(newRefreshToken, user));
        }

        return new AuthResponse(accessToken, newRefreshToken);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
