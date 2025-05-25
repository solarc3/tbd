<script setup lang="ts">
import { useRouter } from "vue-router";
import { ref } from "vue";
import CrearTarea from "@/components/CrearTarea.vue";
import type { Tarea } from "@/api/models";

const router = useRouter();

// redireccion bloques colores
function goToPendingTasks() {
  router.push("/gestor");
}

function goToStatisticsTasks() {
  router.push("/estadisticas");
}

const isModalOpen = ref(false);

const openCreateModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const saveTask = (task: Tarea) => {
  console.log("Nueva tarea creada:", task);
  closeModal();
};

</script>

<template>
  <div class="px-6 py-8">
    <div class="text-center mb-12">
      <h1 class="text-4xl font-bold text-gray-800">¡Bienvenido a tu Organizador de Tareas!</h1>
      <p class="text-lg text-gray-600 mt-4">
        Gestiona tus tareas de manera eficiente y en un sólo lugar. Busca, crea y organiza tus actividades con facilidad.
      </p>
    </div>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
      <!-- tareas pendientes -->
      <div
        class="bg-blue-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer"
        @click="goToPendingTasks"
      >
        <h2 class="text-2xl font-semibold text-blue-800">Tareas Pendientes</h2>
        <p class="text-gray-600 mt-2">
          Revisa las tareas que tienes pendientes y prioriza tus actividades.
        </p>
      </div>

      <!-- crear tarea, modificado para que cree tareas directamente desde este apartado! -->
      <div
          class="bg-green-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer"
          @click="openCreateModal"
      >
        <h2 class="text-2xl font-semibold text-green-800">Crear Nueva Tarea</h2>
        <p class="text-gray-600 mt-2">
          Agrega nuevas tareas y organiza tu día de manera efectiva.
        </p>
      </div>

      <!-- estadisticas -->
      <div
        class="bg-yellow-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer"
        @click="goToStatisticsTasks"
      >
        <h2 class="text-2xl font-semibold text-yellow-800">Estadísticas</h2>
        <p class="text-gray-600 mt-2">
          Descubre las estadísticas y valoraciones en base a tus tareas completadas.
        </p>
      </div>
    </div>


    <!-- tareas cercanas -->
    <div class="mt-12">
      <h3 class="text-2xl font-bold text-gray-800 mb-4">Explora el Mapa</h3>
      <p class="text-gray-600 mt-2">
        Encuentra tareas cercanas a tu ubicación y optimiza tu tiempo.
      </p>
      <div class="bg-gray-200 rounded-lg h-64 flex items-center justify-center">
        <p class="text-gray-500">[Mapa interactivo para tareas cercanas ¿¿]</p>
      </div>
    </div>


    <!-- modal -->
    <div
        v-if="isModalOpen"
        class="fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white p-6 rounded-lg w-full max-w-md shadow-lg relative">
        <CrearTarea @close="closeModal" @save="saveTask" />
      </div>
    </div>
  </div>
</template>

<style scoped>
h1 {
  font-family: "Poppins", sans-serif;
}

.fixed {
  backdrop-filter: blur(4px);
  background-color: rgba(0, 0, 0, 0.5);
}
</style>