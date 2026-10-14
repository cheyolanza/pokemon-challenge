package com.bla.pokemon.infraestructure.persistence.repository;

import com.bla.pokemon.infraestructure.persistence.entity.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataPokemonRepository extends JpaRepository<PokemonEntity, Long> {
}
