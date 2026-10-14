
import {
  BrowserRouter,
  Routes,
  Route,
  NavLink,
  useNavigate,
} from "react-router-dom";

import PokemonList from "./pages/PokemonList";
import PokemonDetail from "./pages/PokemonDetail";
import MyPokemon from "./pages/MyPokemon";
import EditPokemon from "./pages/EditPokemon";
import Login from "./pages/Login";
import Register from "./pages/Register";

import AuthProvider from "./context/AuthProvider";
import { useAuth } from "./context/useAuth";
import ProtectedRoute from "./components/ProtectedRoute";
import { getUsernameFromToken } from "./utils/jtwUtils";
import { UserRound } from "lucide-react";

import "./App.css";

// Navigation component
function Navbar() {
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const username = isAuthenticated? getUsernameFromToken(): null;

  function handleLogout() {
    logout();
    navigate("/");
  }

  return (
    <header className="app-header">
      <div className="app-navbar">
        <NavLink to="/" className="app-logo">
          Pokédex
        </NavLink>

        <nav>
          <NavLink to="/" end>
            Explore Pokémon
          </NavLink>

          {isAuthenticated ? (
            <>
              <NavLink to="/my-pokemon">
                My Pokémon
              </NavLink>

              <>
              <div className="welcome-user">
                <UserRound size={18} />
                <span>
                  Bienvenido, <strong>{username ?? "Usuario"}</strong>
                </span>
              </div>

              <button
                type="button"
                className="nav-button"
                onClick={handleLogout}
              >
                Logout
              </button>
            </>
            </>
          ) : (
            <>
              <NavLink to="/login">
                Login
              </NavLink>

              <NavLink to="/register">
                Register
              </NavLink>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}

// Main application component
export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Navbar />

        <Routes>
          {/* Public routes */}
          <Route
            path="/"
            element={<PokemonList />}
          />

          <Route
            path="/pokemon/:id"
            element={<PokemonDetail />}
          />

          <Route
            path="/login"
            element={<Login />}
          />

          <Route
            path="/register"
            element={<Register />}
          />

          {/* Protected routes */}
          <Route element={<ProtectedRoute />}>
            <Route
              path="/my-pokemon"
              element={<MyPokemon />}
            />

            <Route
              path="/my-pokemon/:id"
              element={<PokemonDetail local />}
            />

            <Route
              path="/my-pokemon/:id/edit"
              element={<EditPokemon />}
            />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
