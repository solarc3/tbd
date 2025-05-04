export interface LoginRequest {
  username: string
  password: string
}

export interface SignupRequest {
  username: string
  email: string
  password: string
}

// User data returned by el servidor
export interface User {
  id: number
  username: string
  email: string
}