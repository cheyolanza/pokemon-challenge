package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;
import com.bla.pokemon.domain.model.Pokemon;

public class UpdatePokemonUseCase {

    private final PokemonRepository repository;

    public UpdatePokemonUseCase(PokemonRepository repository) {
        this.repository = repository;
    }

    public Pokemon execute(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon is required");
        }

        repository.findById(pokemon.id())
                .orElseThrow(() -> new PokemonNotFoundException( pokemon.id()));

        return repository.save(pokemon);
    }
}