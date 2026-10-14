package com.bla.pokemon.presentation.response;

import com.bla.pokemon.application.port.PageResult;
import com.bla.pokemon.domain.model.Pokemon;

import java.util.List;

public record PokemonPageResponse(
        List<PokemonResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static PokemonPageResponse from(PageResult<Pokemon> result){
        return new PokemonPageResponse(
                result.content().stream().map(PokemonResponse::from).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
