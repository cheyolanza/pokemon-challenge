
import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { pokemonApi } from "../api/pokemonApi";
import { useAuth } from "../context/useAuth";
import type { Pokemon } from "../types/pokemon";

interface Props {
  local?: boolean;
}

export default function PokemonDetail({ local = false }: Props) {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  const [pokemon, setPokemon] = useState<Pokemon | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  // Load Pokémon from PokéAPI or PostgreSQL
  useEffect(() => {
    let active = true;

    async function loadPokemon() {
      setLoading(true);
      setError("");
      setMessage("");
      setPokemon(null);

      try {
        const pokemonId = Number(id);

        if (
          !Number.isSafeInteger(pokemonId) ||
          pokemonId <= 0
        ) {
          throw new Error("Invalid Pokémon ID");
        }

        const result = local
          ? await pokemonApi.getLocalById(pokemonId)
          : await pokemonApi.getById(pokemonId);

        if (active) {
          setPokemon(result);
        }
      } catch {
        if (active) {
          setError("Unable to load Pokémon details.");
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
  }, [id, local]);

  // Save Pokémon from PokéAPI to PostgreSQL
  async function handleSync() {
    if (!pokemon || saving) return;

    if (!isAuthenticated) {
      navigate("/login");
      return;
    }

    setSaving(true);
    setError("");
    setMessage("");

    try {
      await pokemonApi.sync(pokemon.id);

      setMessage(
        `${pokemon.name} successfully saved to PostgreSQL.`
      );
    } catch {
      setError("Unable to save Pokémon. Please try again.");
    } finally {
      setSaving(false);
    }
  }

  // Delete Pokémon from PostgreSQL
  async function handleDelete() {
    if (!pokemon || saving) return;

    if (!isAuthenticated) {
      navigate("/login");
      return;
    }

    const confirmed = window.confirm(
      `Are you sure you want to delete ${pokemon.name}?`
    );

    if (!confirmed) return;

    setSaving(true);
    setError("");
    setMessage("");

    try {
      await pokemonApi.delete(pokemon.id);

      navigate("/my-pokemon", { replace: true });
    } catch {
      setError("Unable to delete Pokémon. Please try again.");
    } finally {
      setSaving(false);
    }
  }

  // Navigate to the edit form
  function handleEdit() {
    if (!pokemon) return;

    if (!isAuthenticated) {
      navigate("/login");
      return;
    }

    navigate(`/my-pokemon/${pokemon.id}/edit`);
  }

  if (loading) {
    return (
      <main className="container">
        <p>Loading Pokémon details...</p>
      </main>
    );
  }

  if (error && !pokemon) {
    return (
      <main className="container">
        <Link to={local ? "/my-pokemon" : "/"}>
          ← Back to {local ? "My Pokémon" : "Pokédex"}
        </Link>

        <p role="alert" className="error-message">
          {error}
        </p>
      </main>
    );
  }

  if (!pokemon) {
    return (
      <main className="container">
        <p>Pokémon not found.</p>

        <Link to={local ? "/my-pokemon" : "/"}>
          Go Back
        </Link>
      </main>
    );
  }

  return (
    <main className="container">
      <Link to={local ? "/my-pokemon" : "/"}>
        ← Back to {local ? "My Pokémon" : "Pokédex"}
      </Link>

      <div className="pokemon-detail">
        <img
          src={pokemon.imageUrl}
          alt={pokemon.name}
          loading="lazy"
        />

        <h1>
          {pokemon.name.charAt(0).toUpperCase() +
            pokemon.name.slice(1)}
        </h1>

        <p>
          <strong>Pokédex ID:</strong> #
          {String(pokemon.id).padStart(3, "0")}
        </p>

        <p>
          <strong>Weight:</strong> {pokemon.weight}
        </p>

        <h2>Abilities</h2>

        {pokemon.abilities.length > 0 ? (
          <ul>
            {pokemon.abilities.map((ability) => (
              <li key={ability}>{ability}</li>
            ))}
          </ul>
        ) : (
          <p>No abilities available.</p>
        )}

        <p className="pokemon-source">
          Source: {local ? "PostgreSQL" : "PokéAPI"}
        </p>

        {/* External Pokémon: Sync action */}
        {!local && (
          <button
            type="button"
            className="action-button"
            disabled={saving}
            onClick={handleSync}
          >
            {saving
              ? "Saving..."
              : isAuthenticated
                ? "Save to My Pokémon"
                : "Login to Save"}
          </button>
        )}

        {/* Local Pokémon: Edit and Delete actions */}
        {local && isAuthenticated && (
          <div className="pokemon-actions">
            <button
              type="button"
              className="detail-button"
              disabled={saving}
              onClick={handleEdit}
            >
              Edit
            </button>

            <button
              type="button"
              className="delete-button"
              disabled={saving}
              onClick={handleDelete}
            >
              {saving ? "Deleting..." : "Delete"}
            </button>
          </div>
        )}

        {message && (
          <p role="status" className="success-message">
            {message}
          </p>
        )}

        {error && (
          <p role="alert" className="error-message">
            {error}
          </p>
        )}
      </div>
    </main>
  );
}
