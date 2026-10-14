package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PageResult;
import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListLocalPokemonUseCaseTest {

    @Mock
    private PokemonRepository repository;

    @InjectMocks
    private ListLocalPokemonUseCase useCase;

    @Test
    void shouldReturnPaginatedPokemonFromDatabase() {
        Pokemon pikachu = new Pokemon(
                25L, "pikachu", "image.png",
                60, List.of("static")
        );

        PageResult<Pokemon> expected = new PageResult<>(
                List.of(pikachu), 0, 10, 1, 1
        );

        when(repository.findAll(0, 10)).thenReturn(expected);

        PageResult<Pokemon> result = useCase.execute(0, 10);

        assertEquals(1, result.content().size());
        assertEquals("pikachu", result.content().get(0).name());
        assertEquals(1, result.totalElements());

        verify(repository).findAll(0, 10);
    }

    @Test
    void shouldRejectNegativePage() {
        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(-1, 10)
        );

        verifyNoInteractions(repository);
    }

    @Test
    void shouldRejectInvalidSize() {
        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(0, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(0, 101)
        );

        verifyNoInteractions(repository);
    }
}