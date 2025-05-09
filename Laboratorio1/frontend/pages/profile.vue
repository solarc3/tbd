<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
//import { useRouter } from '#app'
import { Button } from '@/components/ui/button'
import { useAuthStore } from '@/stores/auth'
// importar componente
import type PedidosCliente from '@/components/PedidosCliente.vue'


const shouldRender = ref(false)
const pedidosRef = ref<InstanceType<typeof PedidosCliente> | null>(null)

onMounted(() => {
  shouldRender.value = true
})

const authStore = useAuthStore()
//const router = useRouter()

const userInitials = computed(() => {
  const username = authStore.currentUser?.username || ''
  return username.slice(0, 2).toUpperCase()
})

/*
async function logoutUser() {
  await authStore.logout()
  router.push('/login')
}
*/

function mostrarPedidos() {
  pedidosRef.value?.toggleModal()
}

</script>

<template>
  <ClientOnly>
    <div v-if="shouldRender" class="container mx-auto px-4 py-8">
      <!-- perfil (para hacerlo mas ancho cambiarle el max-w-2xl, especificamente el 2!!)-->
      <div class="max-w-2xl mx-auto bg-white rounded-lg shadow-lg overflow-hidden mb-8">
        <div class="bg-custom p-6 text-white text-center">
          <div
              class="h-16 w-16 mx-auto rounded-full bg-white text-blue-500 flex items-center justify-center text-2xl font-bold"
          >
            {{ userInitials }}
          </div>
          <h1 class="text-2xl font-bold mt-4">{{ authStore.currentUser?.username }}</h1>
          <p class="text-blue-100">{{ authStore.currentUser?.email }}</p>
        </div>

        <div class="p-6 space-y-6">
          <div>
            <h2 class="text-lg font-semibold text-gray-700">Detalles de la cuenta</h2>
            <div class="mt-4 grid grid-cols-2 gap-4 text-sm">
              <div class="text-gray-500">Nombre de usuario:</div>
              <div>{{ authStore.currentUser?.username }}</div>
              <div class="text-gray-500">Correo electrónico:</div>
              <div>{{ authStore.currentUser?.email }}</div>
              <div class="text-gray-500">ID de usuario:</div>
              <div>{{ authStore.currentUser?.id }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- pedidos -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6">
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-lg font-semibold text-gray-700 mb-4">Pedidos</h2>
          <p class="text-gray-600">Aquí puedes ver tus pedidos recientes.</p>
          <Button class="btn-custom mt-4" @click="mostrarPedidos">Ver pedidos</Button>
        </div>

        <!-- boletas -->
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-lg font-semibold text-gray-700 mb-4">Boletas</h2>
          <p class="text-gray-600">Consulta tus boletas de compra.</p>
          <Button class="btn-custom mt-4">Ver boletas</Button>
        </div>

        <!-- estado pedido -->
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-lg font-semibold text-gray-700 mb-4">Estado de un pedido</h2>
          <p class="text-gray-600">Verifica el estado de tus pedidos en curso.</p>
          <Button class="btn-custom mt-4">Ver estado</Button>
        </div>

        <!-- valoraciones que dio el usuario -->
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-lg font-semibold text-gray-700 mb-4">Valoraciones dadas</h2>
          <p class="text-gray-600">Consulta las valoraciones que has realizado.</p>
          <Button class="btn-custom mt-4">Ver valoraciones</Button>
        </div>

        <!-- Componente de Pedidos (inicialmente oculto) -->
        <PedidosCliente ref="pedidosRef" />
      </div>
    </div>
  </ClientOnly>
</template>

<style scoped>
.container {
  background-color: #f9fafb;
  min-height: 100vh;
}
</style>