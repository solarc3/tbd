import { useAuthStore } from '@/stores/auth'
import { defineNuxtRouteMiddleware, navigateTo } from '#app'

export default defineNuxtRouteMiddleware(async (to) => {
  if (import.meta.server) return

  const auth = useAuthStore()

  await auth.initAuth()

  const publicRoutes = ['/login', '/register']
  if (publicRoutes.includes(to.path)) return

  if (!auth.authenticated) {
    return navigateTo('/login')
  }
})
