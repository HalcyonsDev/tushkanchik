package com.halcyon.udemy.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}
