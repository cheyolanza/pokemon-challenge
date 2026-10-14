package com.bla.pokemon.domain.model;

import java.util.UUID;

public record User(
        UUID id,
        String username,
        String email,
        String passwordHash,
        String role
) {
    public User {
        if (id == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash is required");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
    }
}
