<template>
  <header class="border-b bg-[var(--primary)] text-white">
    <nav class="mx-auto flex justify-between px-6 py-2">
      <div class="flex items-center space-x-1">
        <Button variant="link" class="drop-shadow-black">
          <House class="w-2 h-4 mr-1" /> Home
        </Button>
        <Button variant="link">Clientes</Button>
        <Button variant="link">Pedidos</Button>
        <Button variant="link">Farmacias</Button>
      </div>
      <div>
        <AuthDrawer>
          <Button
              variant="default"
              class="flex items-center space-x-2
                bg-[var(--primary)]
                drop-shadow-black
                text-white
                px-4 py-2
                transition-colors duration-200 ease-in-out
                hover:bg-[var(--secondary)]/20">
            <LogIn class="h-5 w-5" />
            <span>Ingresar</span>
          </Button>
        </AuthDrawer>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { LogIn, House } from 'lucide-vue-next'
import AuthDrawer from '../AuthDrawer.vue'
</script>
```