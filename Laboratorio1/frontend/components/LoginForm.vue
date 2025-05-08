<script lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useAuthStore } from '@/stores/auth'
export const containerClass = 'w-full min-h-screen flex items-center justify-center px-4 py-8 bg-gray-50'
</script>

<script setup lang="ts">


const router = useRouter()
const authStore = useAuthStore()
const username = ref('')
const password = ref('')
const error = ref('')

const handleLogin = async () => {
  error.value = ''

  if (!username.value || !password.value) {
    error.value = 'Por favor ingresa tu nombre de usuario y contraseña'
    return
  }

  try {
    await authStore.login(username.value, password.value)
    router.push('/')
  } catch (err: any) {
    console.error('Login error:', err)
    error.value = err.response?.data?.message || 'Error al iniciar sesión, intenta nuevamente'
  }
}
</script>

<template>
  <ClientOnly>
  <div :class="containerClass">
    <div class="w-full max-w-sm sm:max-w-md md:max-w-lg lg:max-w-xl">
      <Card>
        <CardHeader>
          <CardTitle class="text-2xl">Login</CardTitle>
          <CardDescription>Ingresa tu nombre de usuario y contraseña para entrar a tu cuenta</CardDescription>
        </CardHeader>
        <CardContent>
          <form @submit.prevent="handleLogin" class="grid gap-4">
            <div v-if="error" class="p-3 rounded-md bg-red-50 text-red-600 text-sm">
              {{ error }}
            </div>
            <div class="grid gap-2">
              <Label for="username">Username</Label>
              <Input
                id="username"
                v-model="username"
                type="text"
                placeholder="johndoe"
                required
                autocomplete="username"
              />
            </div>
            <div class="grid gap-2">
              <div class="flex items-center">
                <Label for="password">Password</Label>
                <a href="#" class="ml-auto text-sm underline">Olvidaste tu contraseña?</a>
              </div>
              <Input
                id="password"
                v-model="password"
                type="password"
                required
                autocomplete="current-password"
              />
            </div>
            <Button
              type="submit"
              :disabled="authStore.isLoading"
              class="w-full bg-[var(--primary)] transition-colors duration-200 ease-in-out hover:bg-[var(--secondary)]/20"
            >
              {{ authStore.isLoading ? 'Iniciando sesión...' : 'Login' }}
            </Button>
          </form>
          <div class="mt-4 text-center text-sm">
            No tienes una cuenta aun?
            <a href="/register" class="underline">Registrate</a>
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
  </ClientOnly>
</template>
