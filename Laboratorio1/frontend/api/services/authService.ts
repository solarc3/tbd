import apiClient from '@/api/axios'

// Request DTOs
export interface LoginRequest {
  username: string
  password: string
}
export interface SignupRequest {
  username: string
  email: string
  password: string
}

export interface User {
  id: number
  username: string
  email: string
}

class AuthService {
  async login(request: LoginRequest): Promise<User> {
    const { data } = await apiClient.post('/auth/signin', request)
    return data
  }

  async logout(): Promise<void> {
    await apiClient.post('/auth/logout')
  }

  async register(request: SignupRequest): Promise<any> {
    const { data } = await apiClient.post('/auth/signup', request)
    return data
  }

  async me(): Promise<User> {
    const { data } = await apiClient.get('/auth/me')
    return data
  }
}

export default new AuthService()
