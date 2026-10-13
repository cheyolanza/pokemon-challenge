package com.bla.pokemon.domain.exception;

public class PokemonNotFoundException extends RuntimeException {

    public PokemonNotFoundException(Long id) {
        super("Pokemon not found with Id:" + id);
    }
}
