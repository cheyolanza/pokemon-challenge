package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;
import com.bla.pokemon.domain.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLocalPokemonUseCaseTest {

    @Mock
    private PokemonRepository repository;

    @Test
    void shouldReturnPokemonFromDatabase() {
        Pokemon pokemon = new Pokemon(
                25L, "pikachu", "image.png",
                60, List.of("static")
        );

        when(repository.findById(25L)).thenReturn(Optional.of(pokemon));

        GetLocalPokemonUseCase useCase = new GetLocalPokemonUseCase(repository);

        Pokemon result = useCase.execute(25L);

        assertEquals("pikachu", result.name());
        verify(repository).findById(25L);
    }

    @Test
    void shouldThrowNotFoundWhenPokemonIsNotStored() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        GetLocalPokemonUseCase useCase = new GetLocalPokemonUseCase(repository);

        assertThrows(PokemonNotFoundException.class, () -> useCase.execute(999L));
    }
}
