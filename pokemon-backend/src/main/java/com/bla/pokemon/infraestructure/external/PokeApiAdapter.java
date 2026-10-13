
package com.bla.pokemon.infraestructure.external;

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
