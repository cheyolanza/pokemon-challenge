
import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { pokemonApi } from "../api/pokemonApi";
import type { Pokemon } from "../types/pokemon";

export default function EditPokemon() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [pokemon, setPokemon] = useState<Pokemon | null>(null);
  const [abilities, setAbilities] = useState("");
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;

    async function loadPokemon() {
      try {
        const pokemonId = Number(id);

        if (!Number.isSafeInteger(pokemonId) || pokemonId <= 0) {
          throw new Error("Invalid Pokémon ID");
        }

        const result = await pokemonApi.getLocalById(pokemonId);

        if (active) {
          setPokemon(result);
          setAbilities(result.abilities.join(", "));
        }
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
  }, [id]);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!pokemon) return;

    setSaving(true);
    setError("");

    try {
      await pokemonApi.update(pokemon.id, {
        name: pokemon.name,
        imageUrl: pokemon.imageUrl,
        weight: pokemon.weight,
        abilities: abilities
          .split(",")
          .map((value) => value.trim())
          .filter(Boolean),
      });

      navigate(`/my-pokemon/${pokemon.id}`);
    } catch {
      setError("Unable to update Pokémon.");
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return <main className="container">Loading Pokémon...</main>;
  }

  if (!pokemon) {
    return <main className="container">{error || "Pokémon not found."}</main>;
  }

  return (
    <main className="container">
      <Link to={`/my-pokemon/${pokemon.id}`}>
        ← Back to Pokémon
      </Link>

      <div className="auth-container">
        <h1>Edit Pokémon</h1>

        <form onSubmit={handleSubmit}>
          <label htmlFor="edit-name">Name</label>
          <input
            id="edit-name"
            value={pokemon.name}
            onChange={(e) =>
              setPokemon({ ...pokemon, name: e.target.value })
            }
            required
          />

          <label htmlFor="edit-image">Image URL</label>
          <input
            id="edit-image"
            type="url"
            value={pokemon.imageUrl}
            onChange={(e) =>
              setPokemon({ ...pokemon, imageUrl: e.target.value })
            }
            required
          />

          <label htmlFor="edit-weight">Weight</label>
          <input
            id="edit-weight"
            type="number"
            min="1"
            value={pokemon.weight}
            onChange={(e) =>
              setPokemon({
                ...pokemon,
                weight: Number(e.target.value),
              })
            }
            required
          />

          <label htmlFor="edit-abilities">
            Abilities (comma separated)
          </label>
          <input
            id="edit-abilities"
            value={abilities}
            onChange={(e) => setAbilities(e.target.value)}
            required
          />

          {error && <p role="alert" className="error-message">{error}</p>}

          <button type="submit" disabled={saving}>
            {saving ? "Saving..." : "Save Changes"}
          </button>
        </form>
      </div>
    </main>
  );
}
