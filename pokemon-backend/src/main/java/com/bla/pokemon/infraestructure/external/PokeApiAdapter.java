package com.bla.pokemon.infraestructure.external;

import com.bla.pokemon.application.port.PageResult;
import com.bla.pokemon.application.port.PokeApiGateway;
import com.bla.pokemon.domain.model.Pokemon;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Optional;

@Component
public class PokeApiAdapter implements PokeApiGateway {

    private final RestClient restClient;

    public PokeApiAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<Pokemon> findById(Long id) {
        try {
            PokemonApiResponse response = restClient.get()
                    .uri("/pokemon/{id}", id)
                    .retrieve()
                    .body(PokemonApiResponse.class);

            if (response == null) {
                throw new IllegalStateException(
                        "Empty response from PokeAPI"
                );
            }

            List<String> abilities = response.abilities()
                    .stream()
                    .map(a -> a.ability().name())
                    .toList();

            return Optional.of(new Pokemon(
                    response.id(),
                    response.name(),
                    response.sprites().front_default(),
                    response.weight(),
                    abilities
            ));

        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                return Optional.empty();
            }
            throw ex;
        }
    }


    @Override
    public PageResult<Pokemon> findAll(int page, int size) {

        int offset = Math.toIntExact((long) page * size);

        PokemonListApiResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pokemon")
                        .queryParam("offset", offset)
                        .queryParam("limit", size)
                        .build())
                .retrieve()
                .body(PokemonListApiResponse.class);

        if (response == null || response.results() == null
                || response.count() == null) {
            throw new IllegalStateException(
                    "Invalid list response from PokeAPI"
            );
        }

        List<Pokemon> pokemon = response.results()
                .stream()
                .map(item -> {
                    Long id = extractId(item.url());

                    return findById(id).orElseThrow(
                            () -> new IllegalStateException(
                                    "Pokemon disappeared: " + id
                            )
                    );
                })
                .toList();

        int totalPages = (int) Math.ceil(
                (double) response.count() / size
        );

        return new PageResult<>(
                pokemon,
                page,
                size,
                response.count(),
                totalPages
        );
    }

    private Long extractId(String url) {
        String normalized = url.endsWith("/")
                ? url.substring(0, url.length() - 1)
                : url;

        return Long.parseLong(
                normalized.substring(normalized.lastIndexOf('/') + 1)
        );
    }

    public record PokemonListApiResponse(
            Integer count,
            List<PokemonListItem> results
    ) {}

    public record PokemonListItem(
            String name,
            String url
    ) {}


    public record PokemonApiResponse(
            Long id,
            String name,
            Integer weight,
            Sprites sprites,
            List<AbilitySlot> abilities
    ) {}

    public record Sprites(String front_default) {}

    public record AbilitySlot(Ability ability) {}

    public record Ability(String name) {}
}
