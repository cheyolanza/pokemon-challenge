
interface JwtPayload {
    sub?: string;
    username?: string;
    preferred_username?: string;
  }
  
  export function getUsernameFromToken(): string | null {
    const token = sessionStorage.getItem("accessToken");
  
    if (!token) return null;
  
    try {
      const payload = token.split(".")[1];
  
      if (!payload) return null;
  
      const base64 = payload
        .replace(/-/g, "+")
        .replace(/_/g, "/");
  
      const decoded = JSON.parse(atob(base64)) as JwtPayload;
  
      return (
        decoded.username ??
        decoded.preferred_username ??
        null
      );
    } catch {
      return null;
    }
  }
  