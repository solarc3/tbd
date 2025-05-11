<script setup lang="ts">
import { ref, onMounted } from 'vue'
import pedidoService from '@/api/services/pedidoService'
import { useAuthStore } from '@/stores/auth'
import { Button } from '@/components/ui/button'


interface Pedido {
  idPedido: number
  monto: number
  fechaPedido: string
  esUrgente: boolean
  estadoPedido: string
  idCliente: number
  idFarmacia: number
}

const authStore = useAuthStore()
const pedidos = ref<Pedido[]>([])
const loading = ref(false)
const error = ref('')
const showModal = ref(false)
const clienteId = ref<number | null>(null)
const clienteUsername = ref<string>('')

function setClienteId(id: number) {
  clienteId.value = id;
}

async function fetchPedidos() {
  // Usar el ID del cliente proporcionado por el componente padre si está disponible
  const idToUse = clienteId.value || (authStore.currentUser?.id || null);
  
  if (!idToUse) return;
  
  loading.value = true;
  error.value = '';
  
  try {
    // Usar idToUse en lugar de authStore.currentUser.id
    pedidos.value = await pedidoService.getPedidosByCliente(idToUse);
  } catch (err) {
    console.error('Error al obtener pedidos:', err);
    error.value = 'Error al cargar los pedidos. Por favor, intente nuevamente.';
  } finally {
    loading.value = false;
  }
}

function toggleModal() {
  showModal.value = !showModal.value
  if (showModal.value) {
    fetchPedidos()
  }
}

function getEstadoClass(estado: string) {
  switch (estado) {
    case 'ENTREGADO': return 'bg-green-100 text-green-800'
    case 'CANCELADO': return 'bg-red-100 text-red-800'
    case 'CONFIRMADO': return 'bg-blue-100 text-blue-800'
    case 'POR_CONFIRMAR': return 'bg-yellow-100 text-yellow-800'
    default: return 'bg-gray-100 text-gray-800'
  }
}

// Función para formatear la fecha
function formatearFecha(fechaStr: string) {
  if (!fechaStr) return ''
  const fecha = new Date(fechaStr)
  return fecha.toLocaleString()
}

defineExpose({
  toggleModal,
  setClienteId
})
</script>

<template>
  <!-- Modal backdrop -->
  <div v-if="showModal" class="fixed inset-0 bg-black/50 z-40 flex items-center justify-center p-4">
    <!-- Modal content -->
    <div class="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[80vh] overflow-hidden flex flex-col">
      <div class="bg-custom p-4 text-white flex justify-between items-center">
        <h3 class="text-xl font-bold">
          {{ clienteUsername ? `Pedidos de ${clienteUsername}` : 'Mis Pedidos' }}
        </h3>
        <button @click="toggleModal" class="text-white hover:text-gray-200">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <div class="p-4 overflow-auto flex-grow">
        <div v-if="loading" class="flex justify-center items-center h-32">
          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-custom"></div>
        </div>

        <div v-else-if="error" class="bg-red-50 p-4 rounded-md text-red-600">
          {{ error }}
        </div>

        <div v-else-if="pedidos.length === 0" class="text-center py-8 text-gray-500">
          No has realizado ningún pedido aún.
        </div>

        <div v-else class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Fecha</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Monto</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Urgente</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Farmacia</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="pedido in pedidos" :key="pedido.idPedido" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ pedido.idPedido }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ formatearFecha(pedido.fechaPedido) }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${{ pedido.monto }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="['px-2 py-1 rounded-full text-xs font-medium', getEstadoClass(pedido.estadoPedido)]">
                    {{ pedido.estadoPedido }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <span v-if="pedido.esUrgente" class="text-red-600 font-medium">Sí</span>
                  <span v-else>No</span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ pedido.idFarmacia }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="bg-gray-50 p-4 flex justify-end">
        <Button @click="toggleModal" class="btn-custom">Cerrar</Button>
      </div>
    </div>
  </div>
</template>
<PedidosCliente ref="pedidosClienteRef" />