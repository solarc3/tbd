// Auth related types
export interface User {
  id: number;
  username: string;
  email: string;
  token: string;
  refreshToken: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  type: string;
  id: number;
  username: string;
  email: string;
}

// You can add more interfaces for other API responses here