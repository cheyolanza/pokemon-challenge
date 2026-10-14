package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;
import com.bla.pokemon.domain.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletePokemonUseCaseTest {

    @Mock
    private PokemonRepository repository;

    @InjectMocks
    private DeletePokemonUseCase useCase;

    @Test
    void shouldDeleteExistingPokemon() {
        Pokemon pokemon = new Pokemon(
                25L, "pikachu", "image.png", 60,
                List.of("static")
        );

        when(repository.findById(25L)).thenReturn(Optional.of(pokemon));

        useCase.execute(25L);

        verify(repository).deleteById(25L);
    }

    @Test
    void shouldThrow404WhenPokemonDoesNotExist() {
        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(PokemonNotFoundException.class, () -> useCase.execute(999L));

        verify(repository, Mockito.never()).deleteById(ArgumentMatchers.anyLong());
    }
}