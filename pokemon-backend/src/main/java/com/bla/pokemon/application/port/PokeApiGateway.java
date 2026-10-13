package com.bla.pokemon.application.port;

import com.bla.pokemon.domain.model.Pokemon;

import java.util.Optional;

public interface PokeApiGateway {

    Optional<Pokemon> findById(Long id);
}
