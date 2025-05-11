<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { pedidoService, repartidorService } from '@/api/services'
import type { Repartidor } from '@/api/services/repartidorService'
import { useAuthStore } from '@/stores/auth'
import { Button } from '@/components/ui/button'
import { toast } from 'vue-sonner'

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
const error = ref(false)
const showModal = ref(false)
const clienteId = ref<number | null>(null)
const clienteUsername = ref<string>('')
const updatingEstado = ref<number | null>(null)

// Nueva variables para el formulario de entrega
const showEntregarForm = ref(false)
const currentPedido = ref<Pedido | null>(null)
const repartidores = ref<Repartidor[]>([])
const loadingRepartidores = ref(false)
const selectedRepartidor = ref<number | null>(null)
const selectedMetodoPago = ref('')

// Métodos de pago disponibles según data.sql
const metodosPago = [
  'Tarjeta de Crédito',
  'Efectivo',
  'Tarjeta de Débito',
  'Transferencia'
]

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

// Función simulada para obtener repartidores
async function fetchRepartidores(idFarmacia: number) {
  loadingRepartidores.value = true;
  try {
    repartidores.value = await repartidorService.getAllRepartidores();
    
  } catch (err) {
    console.error('Error al cargar repartidores:', err);
    toast.error('No se pudieron cargar los repartidores');
  } finally {
    loadingRepartidores.value = false;
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

async function cambiarEstadoPedido(pedido: Pedido, nuevoEstado: string) {
  if (nuevoEstado === pedido.estadoPedido) return;
  
  // Si el nuevo estado es ENTREGADO, mostramos el formulario de entrega
  if (nuevoEstado === 'ENTREGADO') {
    currentPedido.value = pedido;
    showEntregarForm.value = true;
    // Cargar los repartidores de la farmacia
    await fetchRepartidores(pedido.idFarmacia);
    selectedMetodoPago.value = metodosPago[0]; // Valor por defecto
    return;
  }
  
  updatingEstado.value = pedido.idPedido;
  
  try {
    const success = await pedidoService.cambiarEstadoPedido(pedido.idPedido, nuevoEstado);
    
    if (success) {
      pedido.estadoPedido = nuevoEstado;
      toast.success('Estado actualizado correctamente');
    } else {
      toast.error('Error al actualizar el estado');
    }
  } catch (err) {
    console.error('Error al cambiar estado:', err);
    toast.error('Error al actualizar el estado');
  } finally {
    updatingEstado.value = null;
  }
}

async function handleEntregarPedido() {
  if (!currentPedido.value || !selectedRepartidor.value || !selectedMetodoPago.value) {
    toast.error('Por favor completa todos los campos');
    return;
  }
  
  updatingEstado.value = currentPedido.value.idPedido;
  
  try {
    // Llamar al servicio para entregar el pedido
    await pedidoService.entregarPedido(currentPedido.value.idPedido, {
      idRepartidor: selectedRepartidor.value,
      metodoPago: selectedMetodoPago.value
    });
    
    // Actualizar el estado localmente
    currentPedido.value.estadoPedido = 'ENTREGADO';
    toast.success('Pedido entregado correctamente');
    
    // Cerrar el formulario
    closeEntregarForm();
  } catch (err) {
    console.error('Error al entregar el pedido:', err);
    toast.error('Error al entregar el pedido');
  } finally {
    updatingEstado.value = null;
  }
}

function closeEntregarForm() {
  showEntregarForm.value = false;
  currentPedido.value = null;
  selectedRepartidor.value = null;
  selectedMetodoPago.value = '';
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
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Acciones</th>
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
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <div class="flex items-center">
                    <select
                      class="border border-gray-300 rounded-md px-3 py-1 text-sm focus:ring focus:ring-blue-200"
                      :value="pedido.estadoPedido"
                      @change="(e) => cambiarEstadoPedido(pedido, (e.target as HTMLSelectElement).value)"
                      :disabled="updatingEstado === pedido.idPedido"
                    >
                      <option value="POR_CONFIRMAR">POR_CONFIRMAR</option>
                      <option value="CONFIRMADO">CONFIRMADO</option>
                      <option value="ENTREGADO">ENTREGADO</option>
                      <option value="CANCELADO">CANCELADO</option>
                    </select>
                    
                    <!-- Indicador de carga cuando se está actualizando -->
                    <div v-if="updatingEstado === pedido.idPedido" class="ml-2">
                      <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-custom"></div>
                    </div>
                  </div>
                </td>
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

  <!-- Formulario para entregar pedido -->
  <div v-if="showEntregarForm" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
    <div class="bg-white rounded-lg shadow-xl w-full max-w-md">
      <div class="bg-custom p-4 text-white flex justify-between items-center">
        <h3 class="text-lg font-bold">Entregar Pedido #{{ currentPedido?.idPedido }}</h3>
        <button @click="closeEntregarForm" class="text-white hover:text-gray-200">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <div class="p-6">
        <form @submit.prevent="handleEntregarPedido">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Repartidor</label>
            <div v-if="loadingRepartidores" class="flex items-center space-x-2">
              <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-custom"></div>
              <span class="text-sm text-gray-500">Cargando repartidores...</span>
            </div>
            <select 
              v-else
              v-model="selectedRepartidor"
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option disabled :value="null">Selecciona un repartidor</option>
              <option v-for="repartidor in repartidores" :key="repartidor.idRepartidor" :value="repartidor.idRepartidor">
                {{ repartidor.nombreRepartidor }}
              </option>
            </select>
          </div>

          <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-1">Método de pago</label>
            <select 
              v-model="selectedMetodoPago"
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option disabled value="">Selecciona un método de pago</option>
              <option v-for="metodo in metodosPago" :key="metodo" :value="metodo">
                {{ metodo }}
              </option>
            </select>
          </div>

          <div class="flex justify-end space-x-2">
            <button 
              type="button" 
              @click="closeEntregarForm" 
              class="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300"
            >
              Cancelar
            </button>
            <button 
              type="submit" 
              class="px-4 py-2 bg-custom text-white rounded-md hover:bg-opacity-90"
              :disabled="updatingEstado !== null"
            >
              <span v-if="updatingEstado !== null">
                <span class="inline-block animate-spin mr-2">⟳</span> Procesando...
              </span>
              <span v-else>Entregar Pedido</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>