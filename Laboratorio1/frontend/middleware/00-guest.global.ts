import { useAuthStore } from '@/stores/auth'
import { navigateTo } from '#app'

export default defineNuxtRouteMiddleware((to) => {
  if (import.meta.server) return

  const auth = useAuthStore()
  if (auth.authenticated && ['/login','/register'].includes(to.path)) {
    return navigateTo('/')
  }
})
