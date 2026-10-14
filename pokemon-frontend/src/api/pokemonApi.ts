
import { apiClient } from "./axiosClient";
import type { Pokemon, PokemonPage } from "../types/pokemon";

export const pokemonApi = {
  // External PokéAPI
  getAll: async (page = 0, size = 12): Promise<PokemonPage> => {
    const response = await apiClient.get<PokemonPage>(
      "/api/v1/pokemon",
      { params: { page, size } }
    );

    return response.data;
  },

  getById: async (id: number): Promise<Pokemon> => {
    const response = await apiClient.get<Pokemon>(
      `/api/v1/pokemon/${id}`
    );

    return response.data;
  },

  // PostgreSQL
  getLocal: async (page = 0, size = 12): Promise<PokemonPage> => {
    const response = await apiClient.get<PokemonPage>(
      "/api/v1/pokemon/local",
      { params: { page, size } }
    );

    return response.data;
  },

  getLocalById: async (id: number): Promise<Pokemon> => {
    const response = await apiClient.get<Pokemon>(
      `/api/v1/pokemon/local/${id}`
    );

    return response.data;
  },

sync: async (id: number): Promise<Pokemon> => {
    const response = await apiClient.post<Pokemon>(
      `/api/v1/pokemon/${id}/sync`
    );
    return response.data;
  },
  
  update: async (
    id: number,
    data: Omit<Pokemon, "id">
  ): Promise<Pokemon> => {
    const response = await apiClient.put<Pokemon>(
      `/api/v1/pokemon/local/${id}`,
      data
    );
    return response.data;
  },
  
  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/api/v1/pokemon/local/${id}`);
  },
  
};
