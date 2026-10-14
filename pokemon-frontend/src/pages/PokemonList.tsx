import { useEffect, useState } from "react";
import { pokemonApi } from "../api/pokemonApi";
import PokemonCard from "../components/PokemonCard";
import type { PokemonPage } from "../types/pokemon";

export default function PokemonList() {
  const [page, setPage] = useState(0);
  const [data, setData] = useState<PokemonPage | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;

    async function loadPokemon() {
      setLoading(true);
      setError("");

      try {
        const result = await pokemonApi.getAll(page, 12);
        if (active) setData(result);
      } catch {
        if (active) setError("Unable to load Pokémon.");
      } finally {
        if (active) setLoading(false);
      }
    }

    void loadPokemon();

    return () => {
      active = false;
    };
  }, [page]);

  return (
    <main className="container">
      <h1>Pokédex</h1>
      <p>Explore Pokémon from PokéAPI</p>

      {loading && <p>Loading Pokémon...</p>}
      {error && <p role="alert">{error}</p>}

      {!loading && !error && data && (
        <>
          <div className="pokemon-grid">
            {data.content.map((pokemon) => (
              <PokemonCard key={pokemon.id} pokemon={pokemon} />
            ))}
          </div>

          <div className="pagination">
            <button
              disabled={page === 0}
              onClick={() => setPage((p) => p - 1)}
            >
              Previous
            </button>

            <span>
              Page {page + 1} of {data.totalPages}
            </span>

            <button
              disabled={page + 1 >= data.totalPages}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </button>
          </div>
        </>
      )}
    </main>
  );
}