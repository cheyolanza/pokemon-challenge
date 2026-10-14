package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PageResult;
import com.bla.pokemon.application.port.PokeApiGateway;
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
class ListPokemonUseCaseTest {

    @Mock
    private PokeApiGateway gateway;

    @InjectMocks
    private ListPokemonUseCase useCase;

    @Test
    void shouldReturnPaginatedPokemon(){
        Pokemon pokemon = new Pokemon(
                25L, "pikachu", "image.png",
                60, List.of("static")
        );

        PageResult<Pokemon> page = new PageResult<>(List.of(pokemon), 0,20,100,5);

        when(gateway.findAll(0,20)).thenReturn(page);

        PageResult<Pokemon> result = useCase.execute(0, 20);

        assertEquals(1, result.content().size());
        assertEquals(100, result.totalElements());
        assertEquals(5, result.totalPages());

        verify(gateway).findAll(0, 20);
    }

    @Test
    void shouldRejectNegativePage(){
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(-1, 20));

        verifyNoInteractions(gateway);
    }

    @Test
    void shouldRejectInvalidPageSize(){
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(0, 0));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(0, 101));

        verifyNoInteractions(gateway);
    }

}
