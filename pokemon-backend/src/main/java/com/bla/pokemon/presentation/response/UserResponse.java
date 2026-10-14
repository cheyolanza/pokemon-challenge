package com.bla.pokemon.presentation.response;

import com.bla.pokemon.domain.model.User;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.id(),
                user.username(),
                user.email(),
                user.role()
        );
    }
}
