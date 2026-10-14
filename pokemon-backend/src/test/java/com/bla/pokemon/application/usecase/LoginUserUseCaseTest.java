package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PasswordHasher;
import com.bla.pokemon.application.port.TokenService;
import com.bla.pokemon.application.port.UserRepository;
import com.bla.pokemon.domain.model.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private LoginUserUseCase useCase;

    @Test
    void shouldLoginWithValidCredentials() {
        User user = new User(
                UUID.randomUUID(),
                "jose",
                "jose@example.com",
                "hashed-password",
                "USER"
        );

        when(repository.findByEmail("jose@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordHasher.matches(
                "Password123!",
                "hashed-password"
        )).thenReturn(true);

        when(tokenService.generate(user))
                .thenReturn("jwt-token");

        String token = useCase.execute(
                "jose@example.com",
                "Password123!"
        );

        assertEquals("jwt-token", token);
        verify(tokenService).generate(user);
    }

    @Test
    void shouldRejectInvalidPassword() {
        User user = new User(
                UUID.randomUUID(),
                "jose",
                "jose@example.com",
                "hashed-password",
                "USER"
        );

        when(repository.findByEmail("jose@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordHasher.matches(
                "wrong-password",
                "hashed-password"
        )).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(
                        "jose@example.com",
                        "wrong-password"
                )
        );

        verifyNoInteractions(tokenService);
    }

    @Test
    void shouldRejectUnknownUser() {
        when(repository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(
                        "unknown@example.com",
                        "Password123!"
                )
        );

        verifyNoInteractions(tokenService);
    }
}
