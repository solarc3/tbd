<script setup lang="ts">
import { ref } from 'vue'
import { CheckCircle, Clock, XCircle } from 'lucide-vue-next'

type Pedido = {
  id: number
  cliente: string
  total: number
  estado: 'Pendiente' | 'Enviado' | 'Cancelado'
}

// no modifica nada mas que los estados !! no me centre en nada mas
const pedidos = ref<Pedido[]>([
  { id: 1, cliente: 'Cliente 1', total: 10000, estado: 'Pendiente' },
  { id: 2, cliente: 'Cliente 2', total: 15000, estado: 'Enviado' },
  { id: 3, cliente: 'Cliente 3', total: 20000, estado: 'Cancelado' },
])

function actualizarEstado(id: number, nuevoEstado: Pedido['estado']) {
  const pedido = pedidos.value.find((p) => p.id === id)
  if (pedido) {
    pedido.estado = nuevoEstado
  }
}
</script>

<template>
  <div class="p-6 bg-gray-50 min-h-screen">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">Gestión de Pedidos</h1>
    <div class="overflow-x-auto bg-white shadow-md rounded-lg">
      <table class="min-w-full border border-gray-400">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-600 border-b">ID</th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-600 border-b">Cliente</th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-600 border-b">Total</th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-600 border-b">Estado</th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-600 border-b">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pedido in pedidos" :key="pedido.id" class="hover:bg-gray-50">
            <td class="px-6 py-4 text-sm text-gray-700 border-b">{{ pedido.id }}</td>
            <td class="px-6 py-4 text-sm text-gray-700 border-b">{{ pedido.cliente }}</td>
            <td class="px-6 py-4 text-sm text-gray-700 border-b">${{ pedido.total }}</td>
            <td class="px-6 py-4 text-sm border-b align-middle">
              <span
                :class="{
                  'text-yellow-600': pedido.estado === 'Pendiente',
                  'text-green-600': pedido.estado === 'Enviado',
                  'text-red-600': pedido.estado === 'Cancelado',
                }"
              >
                {{ pedido.estado }}
              </span>
            </td>

            <td class="px-6 py-4 text-sm border-b">
              <select
                  class="border border-gray-300 rounded-md px-3 py-1 text-sm focus:ring focus:ring-blue-200"
                  v-model="pedido.estado"
                  @change="actualizarEstado(pedido.id, pedido.estado)"
              >
                <option value="Pendiente">Pendiente</option>
                <option value="Enviado">Enviado</option>
                <option value="Cancelado">Cancelado</option>
              </select>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
