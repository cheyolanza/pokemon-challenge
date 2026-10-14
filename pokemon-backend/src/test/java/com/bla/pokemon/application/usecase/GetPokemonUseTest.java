package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokeApiGateway;
import com.bla.pokemon.domain.model.Pokemon;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;

import org.junit.jupiter.api.BeforeEach;
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
class GetPokemonUseCaseTest {

    @Mock
    private PokeApiGateway pokeApiGateway;

    @InjectMocks
    private GetPokemonUseCase useCase;

    private Pokemon pokemon;

    @BeforeEach
    void setUp() {
        pokemon = new Pokemon(
                25L,
                "pikachu",
                "https://example.com/pikachu.png",
                60,
                List.of("static", "lightning-rod")
        );
    }

    @Test
    void shouldReturnPokemonWhenExists() {
        when(pokeApiGateway.findById(25L))
                .thenReturn(Optional.of(pokemon));

        Pokemon result = useCase.execute(25L);

        assertEquals(25L, result.id());
        assertEquals("pikachu", result.name());

        verify(pokeApiGateway).findById(25L);
    }

    @Test
    void shouldThrowExceptionWhenPokemonNotFound() {
        when(pokeApiGateway.findById(9999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PokemonNotFoundException.class,
                () -> useCase.execute(9999L)
        );

        verify(pokeApiGateway).findById(9999L);
    }

    @Test
    void shouldRejectInvalidPokemonId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(-1L)
        );

        verifyNoInteractions(pokeApiGateway);
    }
}
