import { useAuthStore } from '@/stores/auth'
import { useRouter } from '#app'

export function useAuth() {
  if (import.meta.server) {
    return {
      user: null,
      isAuthenticated: false,
      isLoading: false,
      login: async () => {},
      logout: async () => {},
      register: async () => {},
      requireAuth: () => {},
      requireGuest: () => {}
    }
  }

  const authStore = useAuthStore()
  const router = useRouter()
  
  // Redirect to login if not authenticated
  const requireAuth = () => {
    if (!authStore.authenticated) {
      router.push('/login')
    }
  }
  
  // Redirect to home if already authenticated
  const requireGuest = () => {
    if (authStore.authenticated) {
      router.push('/')
    }
  }
  
  return {
    user: authStore.currentUser,
    isAuthenticated: authStore.authenticated,
    isLoading: authStore.isLoading,
    login: authStore.login,
    logout: authStore.logout,
    register: authStore.register,
    requireAuth,
    requireGuest
  }
}