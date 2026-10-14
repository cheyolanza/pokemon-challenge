package com.bla.pokemon.presentation.controller;

import com.bla.pokemon.application.usecase.*;
import com.bla.pokemon.domain.model.Pokemon;
import com.bla.pokemon.presentation.request.UpdatePokemonRequest;
import com.bla.pokemon.presentation.response.PokemonPageResponse;
import com.bla.pokemon.presentation.response.PokemonResponse;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    private final GetPokemonUseCase getPokemonUseCase;
    private final ListPokemonUseCase listPokemonUseCase;
    private final SyncPokemonUseCase syncPokemonUseCase;
    private final GetLocalPokemonUseCase getLocalPokemonUseCase;
    private final UpdatePokemonUseCase updatePokemonUseCase;
    private final DeletePokemonUseCase deletePokemonUseCase;
    private final ListLocalPokemonUseCase listLocalPokemonUseCase;

    public PokemonController(GetPokemonUseCase getPokemonUseCase, ListPokemonUseCase listPokemonUseCase,
                             SyncPokemonUseCase syncPokemonUseCase, GetLocalPokemonUseCase getLocalPokemonUseCase,
                             UpdatePokemonUseCase updatePokemonUseCase, DeletePokemonUseCase deletePokemonUseCase,
                             ListLocalPokemonUseCase listLocalPokemonUseCase) {
        this.getPokemonUseCase = getPokemonUseCase;
        this.listPokemonUseCase = listPokemonUseCase;
        this.syncPokemonUseCase = syncPokemonUseCase;
        this.getLocalPokemonUseCase = getLocalPokemonUseCase;
        this.updatePokemonUseCase = updatePokemonUseCase;
        this.deletePokemonUseCase = deletePokemonUseCase;
        this.listLocalPokemonUseCase = listLocalPokemonUseCase;
    }

    @GetMapping("/{id}")
    public PokemonResponse findById(@PathVariable Long id) {
        return PokemonResponse.from(
                getPokemonUseCase.execute(id)
        );
    }

    @GetMapping
    public PokemonPageResponse findAll(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return PokemonPageResponse.from(
                listPokemonUseCase.execute(page, size)
        );
    }

    @PostMapping("/{id}/sync")
    public PokemonResponse syncPokemon(@PathVariable Long id) {
        return PokemonResponse.from(syncPokemonUseCase.execute(id));
    }

    @GetMapping("/local/{id}")
    public PokemonResponse getLocalPokemon(@PathVariable Long id) {
        return PokemonResponse.from(getLocalPokemonUseCase.execute(id));
    }

    @PutMapping("/local/{id}")
    public PokemonResponse updatePokemon(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePokemonRequest request
    ) {
        Pokemon pokemon = new Pokemon(
                id,
                request.name(),
                request.imageUrl(),
                request.weight(),
                request.abilities()
        );

        return PokemonResponse.from(
                updatePokemonUseCase.execute(pokemon)
        );
    }

    @DeleteMapping("/local/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePokemon(@PathVariable Long id) {
        deletePokemonUseCase.execute(id);
    }

    @GetMapping("/local")
    public PokemonPageResponse getLocalPokemonPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return PokemonPageResponse.from(listLocalPokemonUseCase.execute(page, size)
        );
    }
}
