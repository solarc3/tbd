import { defineStore } from "pinia";
import authService from "@/api/services/authService";
import type { User } from "@/api/models";

interface AuthState {
	user: User | null;
	isAuthenticated: boolean;
	loading: boolean;
	initialized: boolean;
}

export const useAuthStore = defineStore("auth", {
	state: (): AuthState => ({
		user: null,
		isAuthenticated: false,
		loading: false,
		initialized: false,
	}),

	getters: {
		currentUser: (state) => state.user,
		authenticated: (state) => state.isAuthenticated,
		isLoading: (state) => state.loading,
	},

	actions: {
		async login(email: string, password: string) {
			this.loading = true;
			try {
				const user = await authService.login({ email, password });
				this.user = user;
				this.isAuthenticated = true;
				return user;
			} finally {
				this.loading = false;
			}
		},

		async logout() {
			this.loading = true;
			try {
				await authService.logout();
			} finally {
				this.user = null;
				this.isAuthenticated = false;
				this.loading = false;
			}
		},

		async register(
			username: string,
			firstName: string,
			lastName: string,
			rut: string,
			email: string,
			password: string,
		) {
			this.loading = true;
			try {
				return await authService.register({
					username,
					firstName,
					lastName,
					rut,
					email,
					password,
				});
			} finally {
				this.loading = false;
			}
		},

		async initAuth() {
			this.loading = true;
			try {
				const user = await authService.me();
				this.user = user;
				this.isAuthenticated = true;
			} catch {
				this.user = null;
				this.isAuthenticated = false;
			} finally {
				this.loading = false;
				this.initialized = true;
			}
		},
	},
});