package com.bla.pokemon.presentation.controller;

import com.bla.pokemon.application.usecase.*;
import com.bla.pokemon.domain.model.Pokemon;
import com.bla.pokemon.infraestructure.config.SecurityConfig;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PokemonController.class)
@Import(SecurityConfig.class)
class PokemonSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetPokemonUseCase getPokemonUseCase;

    @MockitoBean
    private ListPokemonUseCase listPokemonUseCase;

    @MockitoBean
    private SyncPokemonUseCase syncPokemonUseCase;

    @MockitoBean
    private GetLocalPokemonUseCase getLocalPokemonUseCase;

    @MockitoBean
    private ListLocalPokemonUseCase listLocalPokemonUseCase;

    @MockitoBean
    private UpdatePokemonUseCase updatePokemonUseCase;

    @MockitoBean
    private DeletePokemonUseCase deletePokemonUseCase;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void shouldAllowPublicGetWithoutToken() throws Exception {
        Pokemon pokemon = new Pokemon(
                25L,
                "pikachu",
                "https://example.com/pikachu.png",
                60,
                List.of("static")
        );

        when(getPokemonUseCase.execute(25L))
                .thenReturn(pokemon);

        mockMvc.perform(get("/api/v1/pokemon/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("pikachu"));

        verify(getPokemonUseCase).execute(25L);
    }

    @Test
    void shouldRejectSyncWithoutToken() throws Exception {
        mockMvc.perform(
                post("/api/v1/pokemon/25/sync")
        ).andExpect(status().isUnauthorized());

        verifyNoInteractions(syncPokemonUseCase);
    }

    @Test
    void shouldRejectInvalidJwt() throws Exception {

        when(jwtDecoder.decode("invalid-token"))
                .thenThrow(
                        new BadJwtException("Invalid JWT")
                );

        mockMvc.perform(
                        post("/api/v1/pokemon/25/sync")
                                .header(
                                        "Authorization",
                                        "Bearer invalid-token"
                                )
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(syncPokemonUseCase);
    }


    @Test
    void shouldAllowSyncWithValidJwt() throws Exception {
        Instant now = Instant.now();

        Jwt jwt = Jwt.withTokenValue("valid-token")
                .header("alg", "HS256")
                .subject("test-user")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("roles", List.of("USER"))
                .build();

        when(jwtDecoder.decode("valid-token"))
                .thenReturn(jwt);

        Pokemon pokemon = new Pokemon(
                25L,
                "pikachu",
                "https://example.com/pikachu.png",
                60,
                List.of("static")
        );

        when(syncPokemonUseCase.execute(25L))
                .thenReturn(pokemon);

        mockMvc.perform(
                        post("/api/v1/pokemon/25/sync")
                                .header(
                                        "Authorization",
                                        "Bearer valid-token"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(25))
                .andExpect(jsonPath("$.name").value("pikachu"));

        verify(syncPokemonUseCase).execute(25L);
    }
}
