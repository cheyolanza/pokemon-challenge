package com.bla.pokemon.infraestructure.persistence.repository;

import com.bla.pokemon.infraestructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DataUserRepository extends JpaRepository<UserEntity, UUID> {

        Optional<UserEntity> findByEmail(String email);

        boolean existsByEmail(String email);

        boolean existsByUsername(String username);
}
