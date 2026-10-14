package com.bla.pokemon.infraestructure.persistence;

import com.bla.pokemon.application.port.UserRepository;
import com.bla.pokemon.domain.model.User;
import com.bla.pokemon.infraestructure.persistence.entity.UserEntity;
import com.bla.pokemon.infraestructure.persistence.repository.DataUserRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class JpaUserAdapter implements UserRepository {

    private final DataUserRepository repository;

    public JpaUserAdapter(DataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = new UserEntity(
                user.id(),
                user.username(),
                user.email(),
                user.passwordHash(),
                user.role()
        );

        return toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole()
        );
    }
}
