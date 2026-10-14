package com.bla.pokemon.application.port;

import com.bla.pokemon.domain.model.Pokemon;

import java.util.Optional;

/**
 * Definy ops that our app need to persist pokemon
 */
public interface PokemonRepository {

    Pokemon save(Pokemon pokemon);

    Optional<Pokemon> findById(Long id);

    void deleteById(Long id);

    PageResult<Pokemon> findAll(int page, int size);
}
