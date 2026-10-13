package com.bla.pokemon.domain.model;

import java.util.List;

public record Pokemon(
        Long id,
        String name,
        String imageUrl,
        Integer weight,
        List<String> abilities
) {
    public Pokemon{
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Pokemon ID must be positive");
        }

        if (name ==null || name.isBlank()){
            throw new IllegalArgumentException("Pokemon name is required");
        }

        abilities = List.copyOf(abilities==null ? List.of(): abilities);
    }
}
