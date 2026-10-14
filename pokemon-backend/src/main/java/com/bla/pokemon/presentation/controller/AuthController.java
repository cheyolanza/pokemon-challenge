package com.bla.pokemon.presentation.controller;

import com.bla.pokemon.application.usecase.LoginUserUseCase;
import com.bla.pokemon.application.usecase.RegisterUserUseCase;
import com.bla.pokemon.presentation.request.LoginRequest;
import com.bla.pokemon.presentation.request.RegisterUserRequest;
import com.bla.pokemon.presentation.response.LoginResponse;
import com.bla.pokemon.presentation.response.UserResponse;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        return UserResponse.from(
                registerUserUseCase.execute(
                        request.username(),
                        request.email(),
                        request.password()
                )
        );
    }


    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        String token = loginUserUseCase.execute(
                request.email(),
                request.password()
        );

        return new LoginResponse(token, "Bearer");
    }

}
