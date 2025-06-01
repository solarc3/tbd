<template>
  <div class="p-6 bg-gray-50 min-h-screen">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">Estadísticas</h1>
    <!-- tareas y sectores -->
    <div class="flex justify-center space-x-4 mb-6">
      <button
          @click="activeTab = 'tareas'"
          :class="activeTab === 'tareas' ? 'border-blue-500 text-blue-500' : 'border-gray-300 text-gray-700'"
          class="w-full px-6 py-2 bg-white rounded-full border text-lg font-medium shadow-sm"
      >
        Tareas
      </button>
      <button
          @click="activeTab = 'sectores'"
          :class="activeTab === 'sectores' ? 'border-blue-500 text-blue-500' : 'border-gray-300 text-gray-700'"
          class="w-full px-6 py-2 bg-white rounded-full border text-lg font-medium shadow-sm"
      >
        Sectores
      </button>
    </div>

    <div v-if="activeTab === 'tareas'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6">

      <!-- tareas por sector -->
      <div v-if="userTasksBySector.length" class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Tareas por Sector del Usuario</h2>
        <div v-for="item in sortedTasksBySector" :key="item.idSector" class="mb-4">
          <div class="flex justify-between items-center mb-2">
            <p class="font-medium text-sm">{{ item.nombreSector }}</p>
            <p class="text-xs text-gray-600">{{ item.cantidadTareas }} / {{ maxTasks }}</p>
          </div>
          <div class="w-full bg-gray-200 h-4 rounded-full relative">
            <div
                class="bg-blue-500 h-4 rounded-full"
                :style="{ width: (item.cantidadTareas / maxTasks * 100) + '%' }"
            ></div>
          </div>
        </div>
      </div>
      <div v-else class="bg-gray-50 rounded-lg border-2 border-dashed border-gray-300 p-6 text-center">
        <h2 class="text-lg font-semibold text-gray-500 mb-2">Sin Tareas por Sector</h2>
        <p class="text-gray-400">No hay datos de tareas por sector disponibles.</p>
      </div>

      <!-- tarea pendiente mas cercana -->
      <div v-if="closestPendingTask" class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Tarea Pendiente Más Cercana del Usuario</h2>
        <div class="mt-4 grid grid-cols-2 gap-4 text-sm">
          <div class="text-gray-500">Nombre: </div>
          <div>{{ closestPendingTask.tituloTarea }}</div>
          <div class="text-gray-500">ID:  </div>
          <div>{{ closestPendingTask.idTarea }}</div>
          <div class="text-gray-500">Descripción: </div>
          <div>{{ closestPendingTask.descripcionTarea }}</div>
          <div class="text-gray-500">Sector: </div>
          <div>{{ closestPendingTask.nombreSector }}</div>
          <div class="text-gray-500">Distancia: </div>
          <div class="font-medium text-blue-600">
            {{ (closestPendingTask.distanciaAlSectorMetros / 1000).toFixed(2) }} km
          </div>
        </div>
      </div>
      <div v-else class="bg-gray-50 rounded-lg border-2 border-dashed border-gray-300 p-6 text-center">
        <h2 class="text-lg font-semibold text-gray-500 mb-2">Sin Tareas Pendientes</h2>
        <p class="text-gray-400">No hay tareas pendientes en este momento.</p>
      </div>

      <!-- distancia promedio -->
      <div v-if="promDistancia.length > 0" class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Distancia Promedio Tareas Completadas del Usuario</h2>

        <div class="text-center">
          <div class="text-7xl font-bold text-blue-600 leading-none tracking-tight">
            {{ (promDistancia.at(0)?.distanciaPromedio / 1000).toFixed(2) }}
          </div>
          <div class="text-xl text-gray-500 mt-2">kilómetros</div>
        </div>

      </div>
      <div v-else class="bg-gray-50 rounded-lg border-2 border-dashed border-gray-300 p-6 text-center">
        <h2 class="text-lg font-semibold text-gray-500 mb-2">Sin Datos de Distancia</h2>
        <p class="text-gray-400">No hay datos de distancia promedio disponibles.</p>
      </div>
    </div>

    <div v-if="activeTab === 'sectores'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6">

      <!-- sectores con mas tareas completadas -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Sectores con Más Tareas Completadas</h2>
        <div>
          <h3 class="text-md font-medium mb-2">Radio de 2 km</h3>
          <v-chart :option="sectores2kmOptions" style="height: 400px;" />
        </div>
        <div class="mt-6">
          <h3 class="text-md font-medium mb-2">Radio de 5 km</h3>
          <v-chart :option="sectores5kmOptions" style="height: 400px;" />
        </div>
      </div>

      <!-- sectores con mas tareas pendientes -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Sectores con Más Tareas Pendientes</h2>
        <v-chart :option="sectoresPendientesOptions" style="height: 400px;" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import {useAuthStore} from '@/stores/auth';
import {use} from 'echarts/core';
import {CanvasRenderer} from 'echarts/renderers';
import {BarChart} from 'echarts/charts';
import {GridComponent, TitleComponent, TooltipComponent} from 'echarts/components';
import VChart from 'vue-echarts'
import type {ClosestTask, SectorStats, TareaCountBySectorDTO} from '@/api/services/estadisticasService';
import EstadisticasService, {type DistanciaTareaPromedioResponse} from '@/api/services/estadisticasService';

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, TitleComponent]);

const authStore = useAuthStore();
const activeTab = ref<'tareas' | 'sectores'>('tareas');

// tareas por sector
const userTasksBySector = ref<TareaCountBySectorDTO[]>([]);
const maxTasks = computed(() => {
  const counts = userTasksBySector.value.map((item) => item.cantidadTareas || 0);
  return counts.length ? Math.max(...counts) : 0;
});
const sortedTasksBySector = computed(() => {
  return [...userTasksBySector.value].sort((a, b) => b.cantidadTareas - a.cantidadTareas);
});

// tarea pendiente mas cercana, aca idealmente el mapaaa, se puede hacer que el mapa del home muestre esta query tmb
const closestPendingTask = ref<ClosestTask | null>(null);

// sector + completadas
const sectores2km = ref<SectorStats[]>([]);
const sectores5km = ref<SectorStats[]>([]);
const loadingSectores2km = ref(true);
const loadingSectores5km = ref(true);

// seteo grafos
const sectores2kmOptions = ref({
  title: {
    text: 'Sectores con mas tareas completadas (2 km)',
    left: 'center',
  },
  xAxis: {
    type: 'category',
    data: [] as string[],
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      name: 'Tareas Completadas',
      type: 'bar',
      data: [] as number[],
      itemStyle: {
        color: '#54a2eb',
      },
    },
  ],
});

const sectores5kmOptions = ref({
  title: {
    text: 'Sectores con mas tareas completadas (5 km)',
    left: 'center',
  },
  xAxis: {
    type: 'category',
    data: [] as string[],
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      name: 'Tareas Completadas',
      type: 'bar',
      data: [] as number[],
      itemStyle: {
        color: '#8e56ff',
      },
    },
  ],
});

// distancia promedio, nse creo q tenian q verla pq tenia un tema¿¿¿ consultarrr
let promDistancia = ref<DistanciaTareaPromedioResponse[]>([]);

// tareas pendientes por sector
const sectoresPendientes = ref<SectorStats[]>([]);

// seteo grafo
const sectoresPendientesOptions = ref({
  title: {
    text: 'Sectores con más tareas pendientes',
    left: 'center',
  },
  xAxis: {
    type: 'category',
    data: [] as string[],
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      name: 'Tareas Pendientes',
      type: 'bar',
      data: [] as number[],
      itemStyle: {
        color: '#df25ab',
      },
    },
  ],
});

onMounted(async () => {
  try {
    const userId = authStore.currentUser?.id;
    if (userId) {
      userTasksBySector.value = await EstadisticasService.getTareaCountByUserAndSector(userId);
      console.log("Tareas por sector:", userTasksBySector.value);

      const closest = await EstadisticasService.getClosestPendingTask(userId);
      closestPendingTask.value = closest.length ? closest[0] : null;
      console.log("Tarea mas cercana:", closestPendingTask.value);

      // para 2 km
      const data2km = await EstadisticasService.getMostCompletedNearUser(userId, 2);
      sectores2km.value = data2km;
      sectores2kmOptions.value.xAxis.data = sectores2km.value.map((sector) => sector.nombreSector);
      sectores2kmOptions.value.series[0].data = sectores2km.value.map((sector) => sector.cantidadTareas);
      loadingSectores2km.value = false;
      console.log("Sectores con mas tareas completadas (2 km):", data2km);

      // para 5 km
      const data5km = await EstadisticasService.getMostCompletedNearMe(userId, 5);
      sectores5km.value = data5km;
      sectores5kmOptions.value.xAxis.data = sectores5km.value.map((sector) => sector.nombreSector);
      sectores5kmOptions.value.series[0].data = sectores5km.value.map((sector) => sector.cantidadTareas);
      loadingSectores5km.value = false;
      console.log("Sectores con mas tareas completadas (5 km):", data5km);

      promDistancia.value = await EstadisticasService.getAverageDistanceOfCompletedTasks(userId);

      console.log("Promedio de distancia:", promDistancia);

      sectoresPendientes.value = await EstadisticasService.getTasksBySector();
      console.log("Sectores con tareas pendientes:", sectoresPendientes.value);

      sectoresPendientesOptions.value.xAxis.data = sectoresPendientes.value.map((sector) => sector.nombreSector);
      sectoresPendientesOptions.value.series[0].data = sectoresPendientes.value.map((sector) => sector.cantidadTareas);
    }
  } catch (error) {
    console.error("Error:", error);
  }
});

</script>