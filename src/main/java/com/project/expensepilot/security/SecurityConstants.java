package com.project.expensepilot.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;

public class SecurityConstants {
    public static final long JWT_EXPIRATION = 86400000; // 24 hours
    // A static secret key that is at least 512 bits long for HS512
    private static final String JWT_SECRET_STRING = "your-super-secret-and-long-string-that-is-at-least-512-bits-long-for-hs512-algorithm-and-is-very-secure";
    public static final SecretKey JWT_SECRET = Keys.hmacShaKeyFor(JWT_SECRET_STRING.getBytes());
}
