package com.bla.pokemon.infraestructure.config;

import com.bla.pokemon.application.port.*;
import com.bla.pokemon.application.usecase.*;
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

    @Bean
    public ListPokemonUseCase listPokemonUseCase(PokeApiGateway gateway){
        return new ListPokemonUseCase(gateway);
    }

    @Bean
    public SyncPokemonUseCase syncPokemonUseCase(PokeApiGateway gateway, PokemonRepository repository){
        return new SyncPokemonUseCase(repository,gateway);
    }

    @Bean
    public GetLocalPokemonUseCase getLocalPokemonUseCase(PokemonRepository repository){
        return new GetLocalPokemonUseCase(repository);
    }

    @Bean
    public UpdatePokemonUseCase updatePokemonUseCase(PokemonRepository repository){
        return new UpdatePokemonUseCase(repository);
    }

    @Bean
    public DeletePokemonUseCase deletePokemonUseCase(PokemonRepository repository){
        return new DeletePokemonUseCase(repository);
    }

    @Bean
    public ListLocalPokemonUseCase listLocalPokemonUseCase(PokemonRepository repository) {
        return new ListLocalPokemonUseCase(repository);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository repository, PasswordHasher passwordHasher) {
        return new RegisterUserUseCase(repository, passwordHasher);
    }

    @Bean
    public LoginUserUseCase loginUserUseCase(UserRepository repository, PasswordHasher passwordHasher,
                                             TokenService tokenService
    ) {
        return new LoginUserUseCase(repository, passwordHasher, tokenService
        );
    }


}
