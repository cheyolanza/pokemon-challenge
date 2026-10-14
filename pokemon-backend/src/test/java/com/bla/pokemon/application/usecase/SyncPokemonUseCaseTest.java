package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokeApiGateway;
import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;
import com.bla.pokemon.domain.model.Pokemon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncPokemonUseCaseTest {

    @Mock
    private PokeApiGateway gateway;

    @Mock
    private PokemonRepository repository;

    @InjectMocks
    private SyncPokemonUseCase useCase;

    @Test
    void shouldSavePokemonFromPokeApi() {
        Pokemon pikachu = new Pokemon(
                25L,
                "pikachu",
                "https://example.com/25.png",
                60,
                List.of("static", "lightning-rod")
        );

        when(gateway.findById(25L))
                .thenReturn(Optional.of(pikachu));

        when(repository.save(pikachu))
                .thenReturn(pikachu);

        Pokemon result = useCase.execute(25L);

        assertEquals(25L, result.id());
        assertEquals("pikachu", result.name());

        verify(gateway).findById(25L);
        verify(repository).save(pikachu);
    }

    @Test
    void shouldThrowNotFoundWhenPokemonDoesNotExist() {
        when(gateway.findById(999999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PokemonNotFoundException.class,
                () -> useCase.execute(999999L)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldRejectInvalidId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(0L)
        );

        verifyNoInteractions(gateway, repository);
    }
}
