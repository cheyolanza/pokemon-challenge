
package com.bla.pokemon.presentation.response;

import com.bla.pokemon.domain.model.Pokemon;
import java.util.List;

public record PokemonResponse(
        Long id,
        String name,
        String imageUrl,
        Integer weight,
        List<String> abilities
) {
    public static PokemonResponse from(Pokemon pokemon) {
        return new PokemonResponse(
                pokemon.id(),
                pokemon.name(),
                pokemon.imageUrl(),
                pokemon.weight(),
                pokemon.abilities()
        );
    }
}
