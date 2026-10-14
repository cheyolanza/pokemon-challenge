package com.bla.pokemon.infraestructure.external;

import com.bla.pokemon.domain.model.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class PokeApiAdapterTest {

    private MockRestServiceServer server;
    private PokeApiAdapter adapter;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://pokeapi.co/api/v2");

        server = MockRestServiceServer
                .bindTo(builder)
                .build();

        adapter = new PokeApiAdapter(builder.build());
    }

    @Test
    void shouldReturnPokemonWhenApiResponds200() {
        String json = """
            {
              "id": 25,
              "name": "pikachu",
              "weight": 60,
              "sprites": {
                "front_default": "https://example.com/25.png"
              },
              "abilities": [
                {"ability": {"name": "static"}},
                {"ability": {"name": "lightning-rod"}}
              ]
            }
            """;

        server.expect(requestTo(
                        "https://pokeapi.co/api/v2/pokemon/25"))
                .andExpect(method(
                        org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess(
                        json, MediaType.APPLICATION_JSON));

        Optional<Pokemon> result = adapter.findById(25L);

        assertTrue(result.isPresent());
        assertEquals("pikachu", result.get().name());
        assertEquals(60, result.get().weight());
        assertEquals(2, result.get().abilities().size());

        server.verify();
    }

    @Test
    void shouldReturnEmptyWhenApiResponds404() {
        server.expect(requestTo(
                        "https://pokeapi.co/api/v2/pokemon/999999"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        Optional<Pokemon> result = adapter.findById(999999L);

        assertTrue(result.isEmpty());
        server.verify();
    }

    @Test
    void shouldPropagateErrorWhenApiResponds500() {
        server.expect(requestTo(
                        "https://pokeapi.co/api/v2/pokemon/25"))
                .andRespond(withStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(
                HttpServerErrorException.class,
                () -> adapter.findById(25L)
        );

        server.verify();
    }

    @Test
    void shouldRejectEmptyResponse() {
        server.expect(requestTo(
                        "https://pokeapi.co/api/v2/pokemon/25"))
                .andRespond(withSuccess(
                        "", MediaType.APPLICATION_JSON));

        assertThrows(
                IllegalStateException.class,
                () -> adapter.findById(25L)
        );

        server.verify();
    }
}
