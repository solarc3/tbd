<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import MapaTareas from '~/components/MapaTareas.vue'
import wellknown from 'wellknown'

const shouldRender = ref(false)
onMounted(() => {
  shouldRender.value = true;
})

const authStore = useAuthStore();
const userInitials = computed(() => {
  const username = authStore.currentUser?.username || "";
  return username.slice(0, 2).toUpperCase();
});

const ubicacion = computed<string | undefined>(() => {
  return authStore.currentUser?.location;
});

const locationCoords = computed(() => {
  const wkt = ubicacion.value || ''
  const geojson = wellknown(wkt) as
      | {coordinates: [number, number] }


  const [lng, lat] = geojson.coordinates
  return { lat, lng }
});
</script>

<template>
  <div v-if="shouldRender" class="container mx-auto px-4 py-8">
    <div
      class="max-w-2xl mx-auto bg-white rounded-lg shadow-lg overflow-hidden mb-8"
    >
      <div class="bg-custom p-6 text-white text-center">
        <div
          class="h-16 w-16 mx-auto rounded-full bg-white text-blue-500 flex items-center justify-center text-2xl font-bold"
        >
          {{ userInitials }}
        </div>
        <h1 class="text-2xl font-bold mt-4">
          {{ authStore.currentUser?.username }}
        </h1>
        <p class="text-blue-100">{{ authStore.currentUser?.email }}</p>
      </div>

      <div class="p-6 space-y-6">
        <div>
          <h2 class="text-lg font-semibold text-gray-700">
            Detalles de la cuenta
          </h2>
          <div class="mt-4 grid grid-cols-2 gap-4 text-sm">
            <div class="text-gray-500">Nombre de usuario:</div>
            <div>{{ authStore.currentUser?.username }}</div>
            <div class="text-gray-500">Correo electrónico:</div>
            <div>{{ authStore.currentUser?.email }}</div>
            <div class="text-gray-500">ID de usuario:</div>
            <div>{{ authStore.currentUser?.id }}</div>
          </div>

          <div class="w-full h-[40vh] mt-4">
            <MapaTareas
                :initial-lat="locationCoords.lat"
                :initial-lng="locationCoords.lng"
                :initial-zoom="17"
                :editable="false"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  background-color: #f9fafb;
  min-height: 100vh;
}
</style>
