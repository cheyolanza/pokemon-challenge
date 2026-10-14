package com.bla.pokemon.presentation.response;

public record LoginResponse(
        String accessToken,
        String tokenType
) {}