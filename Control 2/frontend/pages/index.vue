<script setup lang="ts">
import { ref } from 'vue'
import { navigateTo } from '#app'
import CrearTarea from '@/components/CrearTarea.vue'
import MapaTareas from '@/components/MapaTareas.vue'
import TareaService from '@/api/services/tareaService'

const isModalOpen = ref(false)
const selectedTask = ref({
  id: 0,
  title: "",
  description: "",
  dueDate: "",
  sector: "",
  estado: "PENDIENTE"
})
const isEditMode = ref(false)

function goToTaskManager() {
  navigateTo('/gestor')
}

const goToStatisticsTasks = () => {
  navigateTo('/estadisticas')
}

const openCreateModal = () => {
  isEditMode.value = false
  selectedTask.value = {
    id: 0,
    title: "",
    description: "",
    dueDate: "",
    sector: "",
    estado: "PENDIENTE"
  }
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
}

interface TaskData {
  title?: string;
  description?: string;
  dueDate?: string;
  sector?: string;
  estado?: string;
  titulo?: string;
  descripcion?: string;
  fechaVencimiento?: string;
  idSector?: number;
  idUsuario?: number;
}

const saveTask = async (task: TaskData) => {
  try {
    console.log('Nueva tarea creada:', task)
    if (task.title && task.description) {
      const tareaToCreate = {
        titulo: task.title,
        descripcion: task.description,
        fechaVencimiento: task.dueDate,
        idSector: task.sector ? parseInt(task.sector as string) : 1,
        estado: "PENDIENTE",
        idUsuario: 1 // Assuming we have a default user ID, or get from auth store
      }
      await TareaService.createTarea(tareaToCreate)
    } else {
      // Task is already in the correct format
      // Safe to use as Partial<Tarea> since the structure matches
      await TareaService.createTarea({
        titulo: task.titulo,
        descripcion: task.descripcion,
        fechaVencimiento: task.fechaVencimiento,
        idSector: task.idSector,
        estado: task.estado,
        idUsuario: task.idUsuario
      })
    }
    closeModal()
  } catch (error) {
    console.error('Error al guardar la tarea:', error)
  }
}

const mapRef = ref(null)
</script>

<template>
  <div class="px-6 py-8">
    <div class="text-center mb-12">
      <h1 class="text-4xl font-bold text-gray-800">
        ¡Bienvenido a tu Organizador de Tareas!
      </h1>
      <p class="text-lg text-gray-600 mt-4">
        Gestiona tus tareas de manera eficiente y en un sólo lugar. Busca, crea y organiza tus actividades con facilidad.
      </p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-4 gap-8 items-stretch">
      <div class="lg:col-span-1 space-y-6">
        <div class="block cursor-pointer" @click="goToTaskManager">
          <div class="bg-blue-100 p-6 rounded-lg shadow-md hover:shadow-lg transition h-32 flex flex-col justify-center">
            <h2 class="text-2xl font-semibold text-blue-800">Tareas Pendientes</h2>
            <p class="text-gray-600 mt-2">Revisa las tareas que tienes pendientes y prioriza tus actividades.</p>
          </div>
        </div>

        <div class="bg-green-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer h-32 flex flex-col justify-center" @click="openCreateModal">
          <h2 class="text-2xl font-semibold text-green-800">Crear Nueva Tarea</h2>
          <p class="text-gray-600 mt-2">Agrega nuevas tareas y organiza tu día de manera efectiva.</p>
        </div>

        <div class="bg-yellow-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer h-32 flex flex-col justify-center" @click="goToStatisticsTasks">
          <h2 class="text-2xl font-semibold text-yellow-800">Estadísticas</h2>
          <p class="text-gray-600 mt-2">Descubre las estadísticas y valoraciones en base a tus tareas completadas.</p>
        </div>
      </div>
      <div class="lg:col-span-3 flex items-center">
        <div class="w-full h-[60vh]" :class="{ 'map-dimmed': isModalOpen }">
          <ClientOnly>
            <MapaTareas
                ref="mapRef"
                :initial-lat="-33.45"
                :initial-lng="-70.6667"
                :initial-zoom="12"
            />
          </ClientOnly>
        </div>
      </div>
    </div>
    <Teleport to="body">
      <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
        <div class="modal-content">
          <CrearTarea 
            :task="selectedTask"
            :is-edit="isEditMode"
            @close="closeModal" 
            @save="saveTask" 
          />
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
h1 {
  font-family: 'Poppins', sans-serif;
}
.modal-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 1rem;
}
.modal-content {
  background: white;
  padding: 1.5rem;
  border-radius: 0.5rem;
  width: 100%;
  max-width: 28rem;
  box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1), 0 10px 10px -5px rgba(0,0,0,0.04);
  max-height: 90vh;
  overflow-y: auto;
}
.map-dimmed {
  filter: brightness(0.7);
  pointer-events: none;
  transition: filter 0.3s ease;
}
:deep(.leaflet-container),
:deep(.leaflet-control-container) {
  z-index: 1000 !important;
}
</style>
