package com.bla.pokemon.application.usecase;

import com.bla.pokemon.application.port.PageResult;
import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.model.Pokemon;

public class ListLocalPokemonUseCase {

    private final PokemonRepository repository;

    public ListLocalPokemonUseCase(PokemonRepository repository) {
        this.repository = repository;
    }

    public PageResult<Pokemon> execute(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        return repository.findAll(page, size);
    }
}