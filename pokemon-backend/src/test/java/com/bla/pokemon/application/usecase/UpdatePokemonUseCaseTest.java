package com.bla.pokemon.application.usecase;

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
class UpdatePokemonUseCaseTest {

    @Mock
    private PokemonRepository repository;

    @InjectMocks
    private UpdatePokemonUseCase useCase;

    @Test
    void shouldUpdateExistingPokemon() {
        Pokemon existing = new Pokemon(
                25L, "pikachu", "old.png", 60,
                List.of("static")
        );

        Pokemon updated = new Pokemon(
                25L, "pikachu-custom", "new.png", 65,
                List.of("static", "lightning-rod")
        );

        when(repository.findById(25L)).thenReturn(Optional.of(existing));

        when(repository.save(updated)).thenReturn(updated);

        Pokemon result = useCase.execute(updated);

        assertEquals("pikachu-custom", result.name());
        assertEquals(65, result.weight());
        verify(repository).save(updated);
    }

    @Test
    void shouldThrow404WhenPokemonDoesNotExist() {
        Pokemon pokemon = new Pokemon(999L, "unknown", null, 10, List.of());

        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PokemonNotFoundException.class, () -> useCase.execute(pokemon));

        verify(repository, never()).save(any());
    }
}