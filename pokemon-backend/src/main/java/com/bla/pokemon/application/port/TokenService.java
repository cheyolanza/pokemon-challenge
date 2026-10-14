package com.bla.pokemon.application.port;

import com.bla.pokemon.domain.model.User;

public interface TokenService {
    String generate(User user);
}
