package com.debesh.snaphire.user_ms.service;

public interface JwtService {
    // Method to generate the token during login
    String generateToken(String userName);
}

