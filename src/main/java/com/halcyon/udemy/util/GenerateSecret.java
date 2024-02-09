package com.halcyon.udemy.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

public class GenerateSecret {
    public static void main(String[] args) {
        System.out.println(generateSecret());
        System.out.println(generateSecret());
    }

    private static String generateSecret() {
        return Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
    }
}
