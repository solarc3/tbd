<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue'
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
} from '@/components/ui/tabs'
import farmaciaService from '@/api/services/farmaciaService'
import { userService } from '@/api/services/userService'
import 'leaflet/dist/leaflet.css'
import type {
  FarmaciaEntity,
  FarmaciaClosestDelivery,
  ClienteZonaCobertura,
  ClienteLejanoDeFarmacia,
} from '@/api/models'

const farmacias = ref<FarmaciaEntity[]>([])
const selectedFarmaciaId = ref<number | null>(null)
const entregasCercanas = ref<FarmaciaClosestDelivery[]>([])

const clienteId = ref<number | null>(null)
const zonaCobertura = ref<ClienteZonaCobertura | null>(null)
const coberturaError = ref<string | null>(null)

const radiusKm = ref(5)
const clientesLejanos = ref<ClienteLejanoDeFarmacia[]>([])

const map1Container = ref<HTMLElement | null>(null)
const map2Container = ref<HTMLElement | null>(null)
const map3Container = ref<HTMLElement | null>(null)
const activeTab = ref('primero')

let L: any
let map1: any
let map2: any
let map3: any

async function initLeaflet() {
  if (!L) {
    const leaflet = await import('leaflet')
    L = leaflet.default

    // Fix for default markers in Leaflet
    const iconRetinaUrl = 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon-2x.png'
    const iconUrl = 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon.png'
    const shadowUrl = 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png'

    L.Icon.Default.mergeOptions({
      iconRetinaUrl,
      iconUrl,
      shadowUrl,
    })
  }
}

function createMap(container: HTMLElement, lat: number, lng: number, zoom = 13) {
  const map = L.map(container).setView([lat, lng], zoom)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 19,
  }).addTo(map)
  return map
}

async function initializeMaps() {
  await nextTick()

  // Initialize map 1 if container exists and map doesn't exist
  if (map1Container.value && !map1) {
    map1 = createMap(map1Container.value, -33.45, -70.65)
  }

  // Initialize map 2 if container exists and map doesn't exist
  if (map2Container.value && !map2) {
    map2 = createMap(map2Container.value, -33.45, -70.65)
  }

  // Initialize map 3 if container exists and map doesn't exist
  if (map3Container.value && !map3) {
    map3 = createMap(map3Container.value, -33.45, -70.65)
  }
}

onMounted(async () => {
  await initLeaflet()
  farmacias.value = await farmaciaService.getAllFarmacias()
  await initializeMaps()
})

// Watch for tab changes to initialize maps when containers become visible
watch(activeTab, async () => {
  await nextTick()
  await initializeMaps()

  // Invalidate map sizes to ensure proper rendering
  setTimeout(() => {
    if (activeTab.value === 'primero' && map1) {
      map1.invalidateSize()
    } else if (activeTab.value === 'segundo' && map2) {
      map2.invalidateSize()
    } else if (activeTab.value === 'tercero' && map3) {
      map3.invalidateSize()
    }
  }, 100)
})

watch(selectedFarmaciaId, async (id) => {
  if (id != null) {
    try {
      entregasCercanas.value = await farmaciaService.getEntregasCercanas(id)
      const farmacia = farmacias.value.find((f) => f.idFarmacia === id)

      if (farmacia?.ubicacion) {
        if (!map1 && map1Container.value) {
          map1 = createMap(map1Container.value, farmacia.ubicacion.y, farmacia.ubicacion.x)
        }

        if (map1) {
          map1.eachLayer((layer: any) => {
            if (layer instanceof L.Marker) {
              map1.removeLayer(layer)
            }
          })

          L.marker([farmacia.ubicacion.y, farmacia.ubicacion.x])
              .addTo(map1)
              .bindPopup(farmacia.nombreFarmacia)
              .openPopup()

          map1.setView([farmacia.ubicacion.y, farmacia.ubicacion.x], 15)
        }
      }
    } catch (e) {
      console.error('Error fetching entregas cercanas:', e)
      entregasCercanas.value = []
    }
  }
})

async function checkZonaCobertura() {
  if (!clienteId.value) return
  try {
    zonaCobertura.value = await userService.getZonaCoberturaByClienteId(clienteId.value)
    coberturaError.value = null

    if (map2 && zonaCobertura.value) {
      // Example: Center map on the zone
      // map2.setView([lat, lng], 13)
    }
  } catch (e: any) {
    zonaCobertura.value = null
    coberturaError.value = 'No encontrado'
  }
}

async function fetchClientesLejanos() {
  try {
    clientesLejanos.value = await userService.getClientesLejanosDeFarmacia(radiusKm.value)

    // Clear existing markers on map3
    if (map3) {
      map3.eachLayer((layer: any) => {
        if (layer instanceof L.Marker || layer instanceof L.Circle) {
          map3.removeLayer(layer)
        }
      })

      // Add markers for distant clients (you'd need actual coordinates)
      // This is just an example, you'll need the actual coordinates from your API
      clientesLejanos.value.forEach((cliente) => {
        // Example: Add markers if you have coordinates
        // L.marker([cliente.lat, cliente.lng])
        //   .addTo(map3)
        //   .bindPopup(`${cliente.nombreCliente} - ${cliente.distanciaKm.toFixed(2)} km`)
      })
    }
  } catch (e) {
    console.error('Error fetching clientes lejanos:', e)
    clientesLejanos.value = []
  }
}
</script>

<template>
  <div class="w-full p-6">
    <Tabs v-model="activeTab" default-value="primero" class="w-full">
      <TabsList class="grid w-full grid-cols-3 h-10 bg-muted p-1 rounded-md">
        <TabsTrigger value="primero" class="rounded-sm">Farmacias</TabsTrigger>
        <TabsTrigger value="segundo" class="rounded-sm">Zona</TabsTrigger>
        <TabsTrigger value="tercero" class="rounded-sm">Lejanos</TabsTrigger>
      </TabsList>

      <TabsContent value="primero" class="mt-6 p-6 bg-background rounded-lg border shadow-sm space-y-4">
        <div>
          <label class="mr-2 font-medium">Seleccione Farmacia:</label>
          <select v-model.number="selectedFarmaciaId" class="border rounded px-3 py-1.5 bg-white">
            <option :value="null" disabled>Seleccione una farmacia</option>
            <option v-for="f in farmacias" :key="f.idFarmacia" :value="f.idFarmacia">
              {{ f.nombreFarmacia }}
            </option>
          </select>
        </div>
        <div ref="map1Container" class="h-96 w-full rounded border" />
        <div v-if="entregasCercanas.length" class="mt-4">
          <h3 class="font-medium mb-2">Entregas Cercanas:</h3>
          <ul class="list-disc ml-5 space-y-1">
            <li v-for="(e, idx) in entregasCercanas" :key="idx" class="text-sm">
              <span class="font-medium">{{ e.nombreUsuario }}</span> - {{ e.distanciaEntrega.toFixed(2) }} m
            </li>
          </ul>
        </div>
      </TabsContent>

      <TabsContent value="segundo" class="mt-6 p-6 bg-background rounded-lg border shadow-sm space-y-4">
        <div class="flex items-center space-x-3">
          <input
              type="number"
              v-model.number="clienteId"
              placeholder="ID Cliente"
              class="border rounded px-3 py-1.5 w-32"
              @keyup.enter="checkZonaCobertura"
          />
          <button
              class="bg-primary text-primary-foreground px-4 py-1.5 rounded hover:bg-primary/90 transition-colors"
              @click="checkZonaCobertura"
          >
            Buscar
          </button>
        </div>
        <div ref="map2Container" class="h-96 w-full rounded border" />
        <div v-if="zonaCobertura" class="p-3 bg-green-50 border border-green-200 rounded">
          <p class="text-sm font-medium text-green-800">
            Cliente: {{ zonaCobertura.nombre }} {{ zonaCobertura.apellido }}
          </p>
          <p class="text-sm text-green-700">Zona: {{ zonaCobertura.nombreSector }}</p>
        </div>
        <div v-else-if="coberturaError" class="p-3 bg-red-50 border border-red-200 rounded">
          <p class="text-sm text-red-600">{{ coberturaError }}</p>
        </div>
      </TabsContent>

      <TabsContent value="tercero" class="mt-6 p-6 bg-background rounded-lg border shadow-sm space-y-4">
        <div class="flex items-center space-x-3">
          <label class="text-sm font-medium">Radio (km):</label>
          <input
              type="number"
              v-model.number="radiusKm"
              class="border rounded px-3 py-1.5 w-20"
              min="1"
              max="50"
          />
          <button
              class="bg-primary text-primary-foreground px-4 py-1.5 rounded hover:bg-primary/90 transition-colors"
              @click="fetchClientesLejanos"
          >
            Buscar Clientes
          </button>
        </div>
        <div ref="map3Container" class="h-96 w-full rounded border" />
        <div v-if="clientesLejanos.length" class="mt-4">
          <h3 class="font-medium mb-2">Clientes más lejanos ({{ clientesLejanos.length }} encontrados):</h3>
          <ul class="list-disc ml-5 space-y-1">
            <li v-for="c in clientesLejanos" :key="c.idCliente" class="text-sm">
              <span class="font-medium">{{ c.nombreCliente }}</span> -
              {{ c.distanciaKm.toFixed(2) }} km desde {{ c.nombreFarmacia }}
            </li>
          </ul>
        </div>
      </TabsContent>
    </Tabs>
  </div>
</template>

<style scoped>
.leaflet-container {
  height: 100%;
  width: 100%;
  z-index: 1;
}

/* Ensure proper rendering of Leaflet controls */
:deep(.leaflet-control-container) {
  position: absolute;
  pointer-events: none;
}

:deep(.leaflet-control) {
  pointer-events: auto;
}
</style>