package com.project.expensepilot.security;

import java.security.Key;

public class SecurityConstants {
    public static final long JWT_EXPIRATION = 70000;
    public static final Key JWT_SECRET = new javax.crypto.spec.SecretKeySpec(
            "secret".getBytes(), "HmacSHA256"
    );
}
