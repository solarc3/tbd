<template>
  <header class="border-b bg-[var(--primary)] text-white">
    <nav class="mx-auto flex justify-between items-center px-6 py-3">
      <div class="flex items-center space-x-4">
        <Button variant="link" @click="router.push('/')">
          <House class="w-5 h-5 mr-2" /> Home
        </Button>
        <Button variant="link" @click="router.push('/clients')">Clientes</Button>
        <Button variant="link" @click="router.push('/productos')">Productos</Button>
        <Button variant="link" @click="router.push('/reportes')">Reportes</Button>
        <Button variant="link" @click="router.push('/repartidores')">Repartidores</Button>
      </div>

      <ClientOnly>
        <div v-if="authStore.authenticated" class="flex items-center gap-4">
          <!-- carrito + mapa -->
          <div class="relative flex items-center">
            <!-- carrito -->
            <button
                @click="router.push('/carrito')"
                class="relative flex items-center"
            >
              <ShoppingCart class="w-6 h-6 text-white" />
              <span
                  v-if="totalCartItems > 0"
                  class="absolute top-0 right-0 bg-red-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center"
              >
                {{ totalCartItems }}
              </span>
            </button>
            <!-- mapa -->
            <button
                @click="router.push('/mapa')"
                class="ml-4 flex items-center"
            >
              <MapPin class="w-6 h-6 text-white" />
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
              class="flex items-center space-x-2 bg-[var(--primary)] drop-shadow-black text-white px-4 py-2 transition-colors duration-200 hover:bg-[var(--secondary)]/20"
              @click="router.push('/login')"
          >
            <LogIn class="h-5 w-5" />
            <span>Ingresar</span>
          </Button>
        </div>
      </ClientOnly>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { LogIn, LogOut, House, User, ShoppingCart, MapPin } from "lucide-vue-next";
import { Button } from "@/components/ui/button";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";
import { computed } from "vue";

const authStore = useAuthStore();
const router = useRouter();
const cartStore = useCartStore();

const totalCartItems = computed(() =>
    cartStore.items.reduce((total, item) => total + item.cantidad, 0)
);

async function handleLogout() {
  try {
    await authStore.logout();
    router.push("/login");
  } catch (err) {
    console.error("Logout error:", err);
  }
}
</script>
