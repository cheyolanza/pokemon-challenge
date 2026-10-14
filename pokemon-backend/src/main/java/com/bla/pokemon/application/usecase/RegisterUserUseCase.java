
package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PasswordHasher;
import com.bla.pokemon.application.port.UserRepository;
import com.bla.pokemon.domain.model.User;

import java.util.Locale;
import java.util.UUID;

public class RegisterUserUseCase {

    private final UserRepository repository;
    private final PasswordHasher passwordHasher;

    public RegisterUserUseCase(
            UserRepository repository,
            PasswordHasher passwordHasher
    ) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
    }

    public User execute(
            String username,
            String email,
            String password
    ) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must contain at least 8 characters");
        }

        String normalizedEmail =
                email.trim().toLowerCase(Locale.ROOT);

        String normalizedUsername = username.trim();

        if (repository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (repository.existsByUsername(normalizedUsername)) {
            throw new IllegalArgumentException("Username already registered");
        }

        User user = new User(
                UUID.randomUUID(),
                normalizedUsername,
                normalizedEmail,
                passwordHasher.hash(password),
                "USER"
        );

        return repository.save(user);
    }
}
