package com.bla.pokemon.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record UpdatePokemonRequest(
        @NotBlank String name,
        String imageUrl,
        @NotNull @Positive Integer weight,
        @NotNull List<@NotBlank String> abilities
) {}