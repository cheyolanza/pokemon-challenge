package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokeApiGateway;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;
import com.bla.pokemon.domain.model.Pokemon;

public class GetPokemonUseCase {
    private final PokeApiGateway pokeApiGateway;

    public GetPokemonUseCase(PokeApiGateway pokeApiGateway) {
        this.pokeApiGateway = pokeApiGateway;
    }

    public Pokemon execute(Long id) {
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Pokemon id must be greater than zero");
        }
        return pokeApiGateway.findById(id)
                .orElseThrow(() -> new PokemonNotFoundException(id));
    }
}
