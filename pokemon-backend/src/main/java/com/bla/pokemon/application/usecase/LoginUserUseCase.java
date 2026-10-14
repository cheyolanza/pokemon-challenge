package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PasswordHasher;
import com.bla.pokemon.application.port.TokenService;
import com.bla.pokemon.application.port.UserRepository;
import com.bla.pokemon.domain.model.User;

import java.util.Locale;

public class LoginUserUseCase {

    private final UserRepository repository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    public LoginUserUseCase(
            UserRepository repository,
            PasswordHasher passwordHasher,
            TokenService tokenService
    ) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.tokenService = tokenService;
    }

    public String execute(String email, String password) {
        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid credentials"
            );
        }

        String normalizedEmail =
                email.trim().toLowerCase(Locale.ROOT);

        User user = repository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid credentials"
                        )
                );

        if (!passwordHasher.matches(
                password, user.passwordHash()
        )) {
            throw new IllegalArgumentException(
                    "Invalid credentials"
            );
        }

        return tokenService.generate(user);
    }
}
