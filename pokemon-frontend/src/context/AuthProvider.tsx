
import { useState } from "react";
import type { ReactNode } from "react";
import { AuthContext } from "./AuthContext";

interface Props {
  children: ReactNode;
}

export default function AuthProvider({ children }: Props) {
  const [token, setToken] = useState<string | null>(() =>
    sessionStorage.getItem("accessToken")
  );

  function login(accessToken: string) {
    sessionStorage.setItem("accessToken", accessToken);
    setToken(accessToken);
  }

  function logout() {
    sessionStorage.removeItem("accessToken");
    setToken(null);
  }

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated: Boolean(token),
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
