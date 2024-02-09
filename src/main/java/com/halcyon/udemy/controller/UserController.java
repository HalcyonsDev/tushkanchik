package com.halcyon.udemy.controller;

import com.halcyon.udemy.model.User;
import com.halcyon.udemy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/balance/top-up")
    public ResponseEntity<User> topUpBalance(@RequestParam BigDecimal amount) {
        User user = userService.topUpBalance(amount);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }
}
