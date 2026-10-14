package com.bla.pokemon.infraestructure.persistence;

import com.bla.pokemon.application.port.PageResult;
import com.bla.pokemon.application.port.PokemonRepository;
import com.bla.pokemon.domain.model.Pokemon;
import com.bla.pokemon.infraestructure.persistence.entity.PokemonEntity;
import com.bla.pokemon.infraestructure.persistence.repository.DataPokemonRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class JpaPokemonAdapter implements PokemonRepository {

    private final DataPokemonRepository repository;

    public JpaPokemonAdapter(DataPokemonRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Pokemon save(Pokemon pokemon) {

        PokemonEntity entity = new PokemonEntity(
                pokemon.id(),
                pokemon.name(),
                pokemon.imageUrl(),
                pokemon.weight(),
                pokemon.abilities()
        );

        PokemonEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pokemon> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Pokemon toDomain(PokemonEntity entity) {
        return new Pokemon(
                entity.getId(),
                entity.getName(),
                entity.getImageUrl(),
                entity.getWeight(),
                entity.getAbilities()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Pokemon> findAll(int page, int size) {

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").ascending()
        );

        Page<PokemonEntity> result =
                repository.findAll(pageable);

        return new PageResult<>(
                result.getContent().stream()
                        .map(this::toDomain)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
