<template>
  <Tabs defaultValue="tasksBySector">
    <TabsList>
      <TabsTrigger value="tasksBySector">Tareas por Sector</TabsTrigger>
      <TabsTrigger value="closestPendingTask">Tarea Más Cercana</TabsTrigger>
    </TabsList>

    <TabsContent value="tasksBySector">
      <Card>
        <CardHeader>
          <CardTitle>
            <Icon name="lucide:bar-chart" class="w-5 h-5 mr-2" />
            Tareas por Sector
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div v-for="sector in tasksBySector" :key="sector.name" class="mb-4">
            <h3 class="font-semibold">{{ sector.name }}</h3>
            <div class="w-full bg-gray-200 rounded-full h-4 mt-2">
              <div
                class="bg-blue-500 h-4 rounded-full"
                :style="{ width: sector.percentage + '%' }"
              ></div>
            </div>
            <p class="text-sm mt-2">{{ sector.tasks }} tareas realizadas</p>
          </div>
        </CardContent>
      </Card>
    </TabsContent>

    <TabsContent value="closestPendingTask">
      <Card>
        <CardHeader>
          <CardTitle>
            <Icon name="lucide:map-pin" class="w-5 h-5 mr-2" />
            Tarea Pendiente Más Cercana
          </CardTitle>
        </CardHeader>
        <CardContent>
          <p v-if="closestPendingTask">
            Tarea: {{ closestPendingTask.title }} <br />
            Descripción: {{ closestPendingTask.description }} <br />
            Distancia: {{ closestPendingTask.distance }} km
          </p>
          <p v-else>No hay tareas pendientes cercanas.</p>
        </CardContent>
      </Card>
    </TabsContent>
  </Tabs>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import EstadisticasService from "@/api/services/estadisticasService";
import { useAuthStore } from "@/stores/auth";

import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

const authStore = useAuthStore();
const tasksBySector = ref<SectorStats[]>([]);
const closestPendingTask = ref<ClosestTask | null>(null);

interface SectorStats {
  name: string;
  percentage: number;
  tasks: number;
}

interface ClosestTask {
  title: string;
  description: string;
  distance: number;
}

const fetchStats = async () => {
  try {
    const userId = authStore.currentUser?.id;
    if (!userId) {
      console.error("No se encontró el ID del usuario.");
      return;
    }

    tasksBySector.value = await EstadisticasService.getTasksBySector();
    closestPendingTask.value =
      await EstadisticasService.getClosestPendingTask(userId);
  } catch (error) {
    console.error("Error al cargar estadísticas:", error);
  }
};

onMounted(() => {
  fetchStats();
});
</script>
Q
