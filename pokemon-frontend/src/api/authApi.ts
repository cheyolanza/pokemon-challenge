import { apiClient } from "./axiosClient";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
}

export const authApi = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>(
      "/api/v1/auth/login",
      data
    );

    return response.data;
  },

  register: async (data: RegisterRequest): Promise<void> => {
    await apiClient.post("/api/v1/auth/register", data);
  },
};
