
package com.bla.pokemon.presentation.controller;

import com.bla.pokemon.application.usecase.GetPokemonUseCase;
import com.bla.pokemon.presentation.response.PokemonResponse;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    private final GetPokemonUseCase getPokemonUseCase;

    public PokemonController(GetPokemonUseCase getPokemonUseCase) {
        this.getPokemonUseCase = getPokemonUseCase;
    }

    @GetMapping("/{id}")
    public PokemonResponse findById(@PathVariable Long id) {
        return PokemonResponse.from(
                getPokemonUseCase.execute(id)
        );
    }
}
