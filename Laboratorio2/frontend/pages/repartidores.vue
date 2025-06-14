<template>
  <div class="container mx-auto p-4">
    <h1 class="text-3xl font-bold mb-6">Listado de Repartidores</h1>

    <div v-if="loading" class="flex justify-center items-center py-8">
      <div class="animate-spin h-8 w-8 border-4 border-primary rounded-full border-t-transparent"></div>
    </div>

    <div v-else-if="error" class="bg-destructive/15 p-4 rounded-md text-destructive mb-4">
      <div class="flex gap-2 items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
        </svg>
        <span>{{ error }}</span>
      </div>
    </div>

    <div v-else-if="repartidores.length === 0" class="bg-primary/15 p-4 rounded-md text-primary mb-4">
      <div class="flex gap-2 items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <span>No hay repartidores registrados.</span>
      </div>
    </div>

    <div v-else>
      <Accordion type="single" collapsible class="w-full">
        <AccordionItem v-for="repartidor in repartidores" :key="repartidor.nombre" :value="repartidor.nombre">
          <AccordionTrigger>
            <div class="flex justify-between items-center w-full pr-4">
              <span>{{ repartidor.nombre }}</span>
              <Badge class="ml-2">{{ repartidor.cantPaquetesEntregados }} paquetes</Badge>
            </div>
          </AccordionTrigger>
          <AccordionContent>
            <div class="grid grid-cols-2 gap-4 py-2">
              <div>
                <p class="text-sm font-medium text-gray-500">Entregas realizadas</p>
                <p class="text-lg font-semibold">{{ repartidor.cantPaquetesEntregados }}</p>
              </div>

              <div>
                <p class="text-sm font-medium text-gray-500">Distancia total</p>
                <p class="text-lg font-semibold">{{ repartidor.distanciaTotal }} km</p>
              </div>

              <div>
                <p class="text-sm font-medium text-gray-500">Tiempo promedio</p>
                <p class="text-lg font-semibold">{{ formatPromedioHoras(repartidor.promedioHoras) }}</p>
              </div>

              <div>
                <p class="text-sm font-medium text-gray-500">Distancia recorrida</p>
                <div class="w-full bg-gray-200 rounded-full h-2.5">
                  <div
                      class="bg-green-500 h-2.5 rounded-full transition-all duration-300"
                      :style="{ width: `${Math.min((repartidor.distanciaTotal / maxDistancia) * 100, 100)}%` }"
                  ></div>
                </div>
              </div>
            </div>

            <div>
              <p class="text-sm font-medium text-gray-500">Eficiencia</p>
              <div class="w-full bg-gray-200 rounded-full h-2.5">
                <div
                    class="bg-blue-500 h-2.5 rounded-full transition-all duration-300"
                    :style="{ width: `${Math.min(repartidor.cantPaquetesEntregados * 25, 100)}%` }"
                ></div>
              </div>
            </div>

          </AccordionContent>
        </AccordionItem>
      </Accordion>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { repartidorService } from '@/api/services'

import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger
} from '@/components/ui/accordion'
import { Badge } from '@/components/ui/badge'

interface RepartidorInfo {
  nombre: string
  cantPaquetesEntregados: number
  promedioHoras: number
  distanciaTotal: number
}

const repartidores = ref<RepartidorInfo[]>([])
const loading = ref(true)
const error = ref('')
const maxDistancia = ref(0)

const formatPromedioHoras = (horas: number): string => {
  if (horas === 0) return 'N/A';

  const hours = Math.floor(horas);
  const minutes = Math.round((horas - hours) * 60);
  
  if (hours === 0) {
    return `${minutes} min`;
  } else if (minutes === 0) {
    return `${hours} h`;
  } else {
    return `${hours} h ${minutes} min`;
  }
}

// añadi la barra verde para la distancia y funciona igual q la de eficiencia en base al repartidor que tenga
// la max distancia, van variando las barrasss !!
onMounted(async () => {
  try {
    loading.value = true;

    const infoRepartidores = await repartidorService.getAllRepartidoresInfo();
    const tiemposRepartidores = await repartidorService.getRepartidorTiempoPromedio();
    const distanciasRepartidores = await repartidorService.getRepartidorDistanciaTotal();

    const tiemposMap = new Map(
      tiemposRepartidores.map(item => [item.nombreRepartidor, item.promedioHoras])
    );

    const distanciasMap = new Map(
      distanciasRepartidores.map(item => [item.nombreRepartidor, Math.round(item.distanciaTotalKm)])
    );

    repartidores.value = infoRepartidores
      .filter(repartidor => distanciasMap.has(repartidor.nombre))
      .map(repartidor => ({
        nombre: repartidor.nombre,
        cantPaquetesEntregados: repartidor.cantPaquetesEntregados,
        promedioHoras: tiemposMap.get(repartidor.nombre) || 0,
        distanciaTotal: distanciasMap.get(repartidor.nombre) || 0
      }));

    maxDistancia.value = Math.max(...repartidores.value.map(r => r.distanciaTotal));

    repartidores.value.sort((a, b) => b.cantPaquetesEntregados - a.cantPaquetesEntregados);
  } catch (err) {
    error.value = 'Error al cargar la lista de repartidores';
    console.error(err);
  } finally {
    loading.value = false;
  }
});
</script>