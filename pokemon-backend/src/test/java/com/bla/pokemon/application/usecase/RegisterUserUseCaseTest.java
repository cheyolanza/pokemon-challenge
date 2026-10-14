
package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PasswordHasher;
import com.bla.pokemon.application.port.UserRepository;
import com.bla.pokemon.domain.model.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordHasher passwordHasher;

    @Test
    void shouldRegisterNewUser() {
        when(repository.existsByEmail("jose@example.com")).thenReturn(false);

        when(repository.existsByUsername("jose")).thenReturn(false);

        when(passwordHasher.hash("Password123!")).thenReturn("hashed-password");

        when(repository.save(any(User.class))).thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        RegisterUserUseCase useCase = new RegisterUserUseCase(repository, passwordHasher);

        User user = useCase.execute("jose", "jose@example.com", "Password123!"
        );

        assertEquals("jose", user.username());
        assertEquals("jose@example.com", user.email());
        assertEquals("hashed-password", user.passwordHash());
        assertEquals("USER", user.role());

        verify(repository).save(any(User.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        when(repository.existsByEmail("jose@example.com")).thenReturn(true);

        RegisterUserUseCase useCase = new RegisterUserUseCase(repository, passwordHasher);

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(
                        "jose",
                        "jose@example.com",
                        "Password123!"
                )
        );

        verify(repository, never()).save(any());
    }
}
