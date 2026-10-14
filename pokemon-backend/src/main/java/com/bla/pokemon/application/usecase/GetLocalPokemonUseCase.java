package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;
import com.bla.pokemon.domain.model.Pokemon;

public class GetLocalPokemonUseCase {

    private final PokemonRepository repository;

    public GetLocalPokemonUseCase(PokemonRepository repository) {
        this.repository = repository;
    }

    public Pokemon execute(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Pokemon ID must be positive");
        }

        return repository.findById(id).orElseThrow(() -> new PokemonNotFoundException(id));
    }
}
