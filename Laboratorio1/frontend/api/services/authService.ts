import apiClient from '@/api/axios'
import type { LoginRequest, SignupRequest, User } from '@/api/models'

class AuthService {
  async login(request: LoginRequest): Promise<User> {
		const { data } = await apiClient.post<User>("/auth/signin", request);
		return data;
	}

  async logout(): Promise<void> {
		await apiClient.post("/auth/logout");
	}

  async register(request: SignupRequest): Promise<any> {
		const { data } = await apiClient.post("/auth/signup", request);
		return data;
	}

  async me(): Promise<User> {
		const { data } = await apiClient.get<User>("/auth/me");
		return data;
	}
}

export default new AuthService()
