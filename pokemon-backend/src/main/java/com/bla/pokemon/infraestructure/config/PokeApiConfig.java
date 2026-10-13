package com.bla.pokemon.infraestructure.config;

import com.bla.pokemon.application.port.PokeApiGateway;
import com.bla.pokemon.application.usecase.GetPokemonUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PokeApiConfig {

    @Bean
    RestClient pokeApiRestClient(){
        return RestClient.builder()
                .baseUrl("https://pokeapi.co/api/v2")
                .build();
    }

    @Bean
    GetPokemonUseCase getPokemonUseCase(
            PokeApiGateway gateway) {
        return new GetPokemonUseCase(gateway);
    }

}
