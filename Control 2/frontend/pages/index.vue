<script setup lang="ts">
import { ref } from 'vue'
import { navigateTo } from '#app'
import CrearTarea from '@/components/CrearTarea.vue'
import MapaTareas from '@/components/MapaTareas.vue'

const isModalOpen = ref(false)

const goToPendingTasks = () => {
  navigateTo('/gestor')
}

const goToStatisticsTasks = () => {
  navigateTo('/estadisticas')
}

const openCreateModal = () => {
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
}

const saveTask = (task: any) => {
  console.log('Nueva tarea creada:', task)
  closeModal()
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
        <NuxtLink to="/gestor" class="block">
          <div class="bg-blue-100 p-6 rounded-lg shadow-md hover:shadow-lg transition h-32 flex flex-col justify-center">
            <h2 class="text-2xl font-semibold text-blue-800">Tareas Pendientes</h2>
            <p class="text-gray-600 mt-2">Revisa las tareas que tienes pendientes y prioriza tus actividades.</p>
          </div>
        </NuxtLink>

        <div class="bg-green-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer h-32 flex flex-col justify-center" @click="openCreateModal">
          <h2 class="text-2xl font-semibold text-green-800">Crear Nueva Tarea</h2>
          <p class="text-gray-600 mt-2">Agrega nuevas tareas y organiza tu día de manera efectiva.</p>
        </div>

        <div class="bg-yellow-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer h-32 flex flex-col justify-center" @click="goToStatisticsTasks">
          <h2 class="text-2xl font-semibold text-yellow-800">Estadísticas</h2>
          <p class="text-gray-600 mt-2">Descubre las estadísticas y valoraciones en base a tus tareas completadas.</p>
        </div>
      </div>
      <div class="lg:col-span-3 h-full flex items-center">
        <div class="h-full w-full h-[60vh]" :class="{ 'map-dimmed': isModalOpen }">
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
          <CrearTarea @close="closeModal" @save="saveTask" />
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
