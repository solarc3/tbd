import { defineNuxtPlugin } from '#app'
import { useAuthStore } from '@/stores/auth'

export default defineNuxtPlugin(() => {
    const auth = useAuthStore()
    auth.initAuth()
})
