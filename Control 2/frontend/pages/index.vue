<template>
  <div class="px-6 py-8">
    <div class="text-center mb-12">
      <h1 class="text-4xl font-bold text-gray-800">¡Bienvenido a tu Organizador de Tareas!</h1>
      <p class="text-lg text-gray-600 mt-4">
        Gestiona tus tareas de manera eficiente y en un sólo lugar. Busca, crea y organiza tus actividades con facilidad.
      </p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8 items-stretch">
      <div class="lg:col-span-1 space-y-6">
        <div
            class="bg-blue-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer h-32 flex flex-col justify-center"
            @click="goToPendingTasks"
        >
          <h2 class="text-2xl font-semibold text-blue-800">Tareas Pendientes</h2>
          <p class="text-gray-600 mt-2">
            Revisa las tareas que tienes pendientes y prioriza tus actividades.
          </p>
        </div>

        <div
            class="bg-green-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer h-32 flex flex-col justify-center"
            @click="openCreateModal"
        >
          <h2 class="text-2xl font-semibold text-green-800">Crear Nueva Tarea</h2>
          <p class="text-gray-600 mt-2">
            Agrega nuevas tareas y organiza tu día de manera efectiva.
          </p>
        </div>

        <div
            class="bg-yellow-100 p-6 rounded-lg shadow-md hover:shadow-lg transition cursor-pointer h-32 flex flex-col justify-center"
            @click="goToStatisticsTasks"
        >
          <h2 class="text-2xl font-semibold text-yellow-800">Estadísticas</h2>
          <p class="text-gray-600 mt-2">
            Descubre las estadísticas y valoraciones en base a tus tareas completadas.
          </p>
        </div>
      </div>

      <div class="lg:col-span-2 h-full">
        <div class="h-full w-full">
          <ClientOnly>
            <MapaTareas />
          </ClientOnly>
        </div>
      </div>
    </div>

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