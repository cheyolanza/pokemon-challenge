package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.exception.PokemonNotFoundException;

public class DeletePokemonUseCase {

    private final PokemonRepository repository;

    public DeletePokemonUseCase(PokemonRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Pokemon ID must be positive");
        }

        repository.findById(id)
                .orElseThrow(() -> new PokemonNotFoundException(id));

        repository.deleteById(id);
    }
}