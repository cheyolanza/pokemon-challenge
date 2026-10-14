package com.bla.pokemon.infraestructure.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pokemon")
public class PokemonEntity {

    @Id
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    private Integer weight;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "pokemon_abilities",
            joinColumns = @JoinColumn(name = "pokemon_id")
    )
    @Column(name = "ability_name", nullable = false)
    private List<String> abilities = new ArrayList<>();

    protected PokemonEntity() {
    }

    public PokemonEntity(
            Long id,
            String name,
            String imageUrl,
            Integer weight,
            List<String> abilities
    ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.weight = weight;
        this.abilities = new ArrayList<>(abilities);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getWeight() {
        return weight;
    }

    public List<String> getAbilities() {
        return abilities;
    }

}
