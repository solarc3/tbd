<template>
  <header class="border-b bg-[var(--primary)] text-white">
    <nav class="mx-auto flex justify-between px-6 py-2">
      <div class="flex items-center space-x-1">
        <Button variant="link" @click="router.push('/')">
          <House class="w-4 h-4 mr-1" /> Home
        </Button>
        <Button variant="link">Clientes</Button>
        <Button variant="link">Pedidos</Button>
        <Button variant="link">Farmacias</Button>
      </div>

      <div v-if="authStore.authenticated">
        <div class="flex items-center space-x-3">
          <span class="text-sm font-medium">{{ authStore.currentUser?.username }}</span>
          <Button
              variant="default"
              class="bg-blue-500 hover:bg-blue-600 text-white mr-2"
              @click="router.push('/profile')"
          >
            <User class="h-5 w-5 mr-1" />
            Profile
          </Button>
          <Button
              variant="default"
              class="flex items-center space-x-2
                   bg-red-500 drop-shadow-black text-white
                   px-4 py-2 transition-colors duration-200
                   hover:bg-red-600"
              @click="handleLogout"
          >
            <LogOut class="h-5 w-5" />
            <span>Cerrar sesión</span>
          </Button>
        </div>
      </div>

      <div v-else>
        <Button
            variant="default"
            class="flex items-center space-x-2
                 bg-[var(--primary)] drop-shadow-black text-white
                 px-4 py-2 transition-colors duration-200
                 hover:bg-[var(--secondary)]/20"
            @click="router.push('/login')"
        >
          <LogIn class="h-5 w-5" />
          <span>Ingresar</span>
        </Button>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { LogIn, LogOut, House, User } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

async function handleLogout() {
  try {
    await authStore.logout()
    router.push('/login')
  } catch (err) {
    console.error('Logout error:', err)
  }
}
</script>
