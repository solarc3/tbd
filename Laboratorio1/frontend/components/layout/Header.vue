<template>
  <header class="border-b bg-[var(--primary)] text-white">
    <nav class="mx-auto flex justify-between items-center px-6 py-3">
      <div class="flex items-center space-x-4">
        <Button variant="link" @click="router.push('/')">
          <House class="w-5 h-5 mr-2" /> Home
        </Button>
        <Button variant="link">Clientes</Button>
        <Button variant="link" @click="router.push('/productos')">Productos</Button>
        <Button variant="link" @click="router.push('/pedidos')">Pedidos</Button>
        <Button variant="link" @click="router.push('/reportes')">Reportes</Button>
      </div>

      <!-- genera problemas de hidration, se debe hacer client side para evitar que se genera un missmatch -->
      <ClientOnly>
        <div v-if="authStore.authenticated" class="flex items-center gap-4">
          <!-- carrito -->
          <div class="relative flex items-center">
            <button @click="router.push('/carrito')" class="relative flex items-center">
              <ShoppingCart class="w-6 h-6 text-white" />
              <span
                  v-if="cartItems.length"
                  class="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center"
              >
                {{ cartItems.length }}
              </span>
            </button>
          </div>

          <span class="text-sm font-medium">{{ authStore.currentUser?.username }}</span>

          <Button
              variant="default"
              class="bg-blue-500 text-white flex items-center px-3 py-2 space-x-2 hover:bg-blue-600"
              @click="router.push('/profile')"
          >
            <User class="h-5 w-5" />
          </Button>

          <Button
              variant="default"
              class="bg-red-500 text-white flex items-center px-3 py-2 space-x-2 hover:bg-red-600"
              @click="handleLogout"
          >
            <LogOut class="h-5 w-5" />
          </Button>
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

        <!-- Fallback content for server-side rendering -->
        <template #fallback>
          <div class="flex items-center">
            <Button
              variant="default"
              class="flex items-center space-x-2
                   bg-[var(--primary)] drop-shadow-black text-white
                   px-4 py-2 transition-colors duration-200
                   hover:bg-[var(--secondary)]/20"
            >
              <LogIn class="h-5 w-5" />
              <span>Ingresar</span>
            </Button>
          </div>
        </template>
      </ClientOnly>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { LogIn, LogOut, House, User, ShoppingCart } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

// Initialize with empty array to avoid hydration issues
const cartItems = ref([])

onMounted(() => {
  // Initialize cart items after component is mounted (client-side only)
  cartItems.value = [
    { id: 1, name: 'Producto A', price: 10000, quantity: 2 },
    { id: 2, name: 'Producto B', price: 15000, quantity: 1 },
  ]
})

async function handleLogout() {
  try {
    await authStore.logout()
    router.push('/login')
  } catch (err) {
    console.error('Logout error:', err)
  }
}
</script>
