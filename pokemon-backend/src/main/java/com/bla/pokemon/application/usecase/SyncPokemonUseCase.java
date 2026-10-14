package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokeApiGateway;
import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;
import com.bla.pokemon.domain.model.Pokemon;

public class SyncPokemonUseCase {
    private final PokemonRepository pokemonRepository;
    private final PokeApiGateway pokeApiGateway;

    public SyncPokemonUseCase(PokemonRepository pokemonRepository, PokeApiGateway pokeApiGateway) {
        this.pokemonRepository = pokemonRepository;
        this.pokeApiGateway = pokeApiGateway;
    }

    public Pokemon execute(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Pokemon ID must be positive"
            );
        }

        Pokemon pokemon = pokeApiGateway.findById(id)
                .orElseThrow(() ->
                        new PokemonNotFoundException(id)
                );

        return pokemonRepository.save(pokemon);
    }

}
