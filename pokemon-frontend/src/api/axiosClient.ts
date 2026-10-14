
import axios from "axios";

export const apiClient = axios.create({
  baseURL: "",
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.request.use((config) => {
  const token = sessionStorage.getItem("accessToken");

  if (token) {
    config.headers.set("Authorization", `Bearer ${token}`);
  }

  return config;
});


apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
      if (
        error.response?.status === 401 &&
        !error.config?.url?.includes("/api/v1/auth/login")
      ) {
        sessionStorage.removeItem("accessToken");
        window.location.assign("/login");
      }
  
      return Promise.reject(error);
    }
  );
  