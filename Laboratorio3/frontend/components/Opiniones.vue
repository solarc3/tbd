<template>
  <transition name="fade">
    <div v-if="visible" class="fixed bottom-6 right-6 z-50 bg-white rounded-lg shadow-lg p-6 w-80 border border-gray-200">
      <div class="flex justify-between items-center mb-2">
        <span class="font-semibold text-gray-800">¡Deja tu opinión!</span>
        <button @click="close" class="text-gray-400 hover:text-gray-600">&times;</button>
      </div>
      <div v-if="!submitted">
        <div v-for="producto in productos" :key="producto.idProducto" class="mb-4">
          <div class="text-sm text-gray-700 mb-1">{{ producto.nombre }}</div>
          <select v-model="opiniones[producto.idProducto].puntuacion" class="w-full mb-1 border rounded px-2 py-1">
            <option disabled value="">Puntaje</option>
            <option v-for="n in 5" :key="n" :value="n">{{ n }} ⭐</option>
          </select>
          <input
            v-model="opiniones[producto.idProducto].comentario"
            type="text"
            class="w-full border rounded px-2 py-1 text-sm"
            placeholder="Comentario"
          />
        </div>
        <button
          @click="enviarOpiniones"
          class="w-full bg-custom text-white rounded py-2 mt-2 hover:bg-custom2 transition"
        >
          Enviar opiniones
        </button>
      </div>
      <div v-else class="text-green-600 text-center py-4">
        ¡Gracias por tu opinión!
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import farmaciaService from '@/api/services/farmaciaService'
import { useAuthStore } from '@/stores/auth'

const props = defineProps<{
  productos: { idProducto: number; nombre: string }[]
}>()

const authStore = useAuthStore()
const visible = ref(true)
const submitted = ref(false)
const opiniones = reactive<{ [id: number]: { puntuacion: number | ''; comentario: string } }>(
  Object.fromEntries(props.productos.map(p => [p.idProducto, { puntuacion: '', comentario: '' }]))
)

function close() {
  visible.value = false
  localStorage.setItem('opinionesPopupClosed', '1')
}

async function enviarOpiniones() {
  for (const id in opiniones) {
    const { puntuacion, comentario } = opiniones[id]
    if (puntuacion) {
      await farmaciaService.createOpinion({
        idProducto: Number(id),
        puntuacion,
        comentario,
        idCliente: authStore.user?.id
      })
    }
  }
  submitted.value = true
  setTimeout(close, 2000)
}
</script>

<style scoped>
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>