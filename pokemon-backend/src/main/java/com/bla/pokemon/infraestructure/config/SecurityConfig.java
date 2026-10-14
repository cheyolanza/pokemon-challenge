package com.bla.pokemon.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/**")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth/register",
                                "/api/v1/auth/login"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/pokemon/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> {})
                )
                .build();

        /*
        return http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/**")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/pokemon/**"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/pokemon/* /sync"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/pokemon/local/*"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/pokemon/local/*"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth/register"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
        */
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
