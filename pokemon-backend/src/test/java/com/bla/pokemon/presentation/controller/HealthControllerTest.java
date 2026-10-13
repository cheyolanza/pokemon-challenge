package com.bla.pokemon.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    private final HealthController controller =
            new HealthController();

    @Test
    @WithMockUser
    void shouldReturnHealthStatus() throws Exception {
        var response = controller.health();

        assertEquals(HttpStatus.OK,
                response.getStatusCode());

        assertEquals("Pokemon API is running",
                response.getBody());
    }
}
