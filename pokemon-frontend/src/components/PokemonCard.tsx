
import { Link } from "react-router-dom";
import type { Pokemon } from "../types/pokemon";

interface Props {
  pokemon: Pokemon;
  local?: boolean;
}

export default function PokemonCard({
  pokemon,
  local = false,
}: Props) {
  return (
    <article className="pokemon-card">
      <img
        src={pokemon.imageUrl}
        alt={pokemon.name}
        loading="lazy"
      />

      <span>
        #{String(pokemon.id).padStart(3, "0")}
      </span>

      <h3>{pokemon.name}</h3>

      <p>Weight: {pokemon.weight}</p>

      <Link
        to={
          local
            ? `/my-pokemon/${pokemon.id}`
            : `/pokemon/${pokemon.id}`
        }
        className="detail-button"
      >
        View Details
      </Link>
    </article>
  );
}
