
CREATE TABLE pokemon (
     id BIGINT PRIMARY KEY,
     name VARCHAR(150) NOT NULL,
     image_url TEXT,
     weight INTEGER,
     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
     updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE pokemon_abilities (
   pokemon_id BIGINT NOT NULL,
   ability_name VARCHAR(150) NOT NULL,

   CONSTRAINT pk_pokemon_abilities
       PRIMARY KEY (pokemon_id, ability_name),

   CONSTRAINT fk_pokemon_abilities_pokemon
       FOREIGN KEY (pokemon_id)
           REFERENCES pokemon(id)
           ON DELETE CASCADE
);

CREATE INDEX idx_pokemon_name
    ON pokemon(name);
