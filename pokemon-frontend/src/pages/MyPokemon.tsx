
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { pokemonApi } from "../api/pokemonApi";
import PokemonCard from "../components/PokemonCard";
import type { PokemonPage } from "../types/pokemon";

export default function MyPokemon() {
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
        const result = await pokemonApi.getLocal(page, 12);

        if (active) {
          setData(result);
        }
      } catch {
        if (active) {
          setError("Unable to load saved Pokémon.");
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    }

    void loadPokemon();

    return () => {
      active = false;
    };
  }, [page]);

  return (
    <main className="container">
      <h1>My Pokémon</h1>
      <p>Pokémon stored in PostgreSQL</p>

      {loading && <p>Loading saved Pokémon...</p>}

      {error && <p role="alert">{error}</p>}

      {!loading && !error && data && (
        <>
          {data.content.length === 0 ? (
            <div className="empty-state">
              <h2>No Pokémon saved yet</h2>
              <p>Explore Pokémon and add them to your collection.</p>
              <Link to="/">Explore Pokémon</Link>
            </div>
          ) : (
            <>
              <div className="pokemon-grid">
                {data.content.map((pokemon) => (
                  <PokemonCard
                    key={pokemon.id}
                    pokemon={pokemon}
                    local
                  />
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
        </>
      )}
    </main>
  );
}
