<template>
  <div class="notification-panel bg-white rounded-lg shadow-lg p-4 w-80 max-h-96 overflow-y-auto">
    <div class="flex justify-between items-center mb-3 border-b pb-2">
      <h3 class="font-bold text-lg text-slate-800">Tareas por vencer hoy</h3>
      <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700">
        <XIcon class="h-5 w-5" />
      </button>
    </div>
    
    <div v-if="loading" class="flex justify-center py-4">
      <div class="loader"></div>
    </div>
    
    <div v-else-if="error" class="text-red-500 py-3 text-center">
      {{ error }}
    </div>
    
    <div v-else-if="tareas.length === 0" class="text-gray-500 py-3 text-center">
      No tienes tareas por vencer hoy
    </div>
    
    <div v-else class="space-y-3">
      <div 
        v-for="tarea in tareas" 
        :key="tarea.id"
        class="bg-red-50 border border-red-200 rounded-md p-3 relative hover:bg-red-100 transition-colors"
      >
        <h4 class="font-semibold text-gray-600 text-md">{{ tarea.titulo }}</h4>
        <p class="text-sm text-gray-600 mt-1">{{ tarea.descripcion }}</p>
        <div class="mt-2 text-xs text-red-600 font-medium">
          Tiempo restante: 
          <span v-if="tarea.horasRestantes > 0">{{ tarea.horasRestantes }}h </span>
          <span>{{ tarea.minutosRestantes }}m {{ tarea.segundosRestantes }}s</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { XIcon } from 'lucide-vue-next';
import TareaService, { type TareaVencimiento } from '@/api/services/tareaService';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const tareas = ref<TareaVencimiento[]>([]);
const loading = ref(true);
const error = ref('');
const refreshInterval = ref<number | null>(null);

const fetchTareas = async () => {
  if (!authStore.currentUser?.id) return;
  
  try {
    loading.value = true;
    error.value = '';
    tareas.value = await TareaService.getTareasPorVencerHoy(authStore.currentUser.id);
  } catch (err) {
    error.value = 'Error al cargar las notificaciones';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchTareas();
  // Refrescar cada minuto para actualizar los tiempos restantes
  refreshInterval.value = window.setInterval(fetchTareas, 60000);
});

onUnmounted(() => {
  if (refreshInterval.value) {
    clearInterval(refreshInterval.value);
  }
});

defineEmits(['close']);
</script>

<style scoped>
.loader {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.notification-panel {
  z-index: 50;
}
</style>