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
  FarmaciaClosestDeliveryResponse,
  ClienteZonaCobertura,
  ClienteLejanoDeFarmacia,
} from '@/api/models'

const farmacias = ref<FarmaciaEntity[]>([])
const selectedFarmaciaId = ref<number | null>(null)
const entregasCercanas = ref<FarmaciaClosestDeliveryResponse[]>([])

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
let markers1: any[] = []
let markers2: any[] = []
let markers3: any[] = []

async function initializeMap1() {
  if (!import.meta.client || !map1Container.value || map1) return

  const leaflet = await import('leaflet')
  L = leaflet.default

  // Fix for default marker icons in Vue/Vite
  delete (L.Icon.Default.prototype as any)._getIconUrl
  L.Icon.Default.mergeOptions({
    iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon-2x.png',
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
  })

  map1 = L.map(map1Container.value).setView([-33.45, -70.65], 13)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 19
  }).addTo(map1)
}

async function initializeMap2() {
  if (!import.meta.client || !map2Container.value || map2) return

  const leaflet = await import('leaflet')
  if (!L) L = leaflet.default

  map2 = L.map(map2Container.value).setView([-33.45, -70.65], 13)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 19
  }).addTo(map2)

  map2.invalidateSize()
}

async function initializeMap3() {
  if (!import.meta.client || !map3Container.value || map3) return

  const leaflet = await import('leaflet')
  if (!L) L = leaflet.default

  map3 = L.map(map3Container.value).setView([-33.45, -70.65], 11)

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 19
  }).addTo(map3)

  map3.invalidateSize()
}

function clearMapLayers(map: any, markers: any[]) {
  if (!map) return
  markers.forEach(marker => map.removeLayer(marker))
  markers.length = 0
}

onMounted(async () => {
  console.log('Component mounted')

  try {
    console.log('Fetching farmacias...')
    farmacias.value = await farmaciaService.getAllFarmacias()
    console.log('Farmacias loaded:', farmacias.value.length)
  } catch (error) {
    console.error('Error fetching farmacias:', error)
  }

  await nextTick()

  // Add a small delay to ensure DOM is ready
  setTimeout(() => {
    if (import.meta.client) {
      console.log('Attempting to initialize map after mount')
      initializeMap1()
    }
  }, 100)
})

watch(activeTab, async (newTab) => {
  await nextTick()

  setTimeout(async () => {
    if (newTab === 'primero') {
      if (!map1) await initializeMap1()
      else map1.invalidateSize()
    } else if (newTab === 'segundo') {
      if (!map2) await initializeMap2()
      else map2.invalidateSize()
    } else if (newTab === 'tercero') {
      if (!map3) await initializeMap3()
      else map3.invalidateSize()
    }
  }, 100)
})

watch(selectedFarmaciaId, async (id) => {
  if (id == null || !map1) return

  try {
    entregasCercanas.value = await farmaciaService.getEntregasCercanas(id)

    // Find the selected farmacia
    const farmacia = farmacias.value.find(f => f.idFarmacia === id)

    if (farmacia) {
      clearMapLayers(map1, markers1)

      // Determine farmacia coordinates - check if we have them from the response or need to use the farmacia entity
      let farmaciaLat, farmaciaLng;

      if (entregasCercanas.value.length > 0 && entregasCercanas.value[0].ubicacionFarmacia) {
        // Use coordinates from the response
        farmaciaLat = entregasCercanas.value[0].ubicacionFarmacia.latitude;
        farmaciaLng = entregasCercanas.value[0].ubicacionFarmacia.longitude;
      } else if (farmacia.ubicacion) {
        // Use coordinates from farmacia entity (x, y format)
        farmaciaLat = farmacia.ubicacion.y;
        farmaciaLng = farmacia.ubicacion.x;
      } else {
        console.error('No coordinates found for farmacia');
        return;
      }

      // Add farmacia marker
      const farmaciaMarker = L.marker([farmaciaLat, farmaciaLng])
          .addTo(map1)
          .bindPopup(`<b>${farmacia.nombreFarmacia}</b><br>${farmacia.direccion}`)
          .openPopup()

      markers1.push(farmaciaMarker)

      // Add delivery markers from entregasCercanas
      entregasCercanas.value.forEach((entrega) => {
        if (entrega.ubicacionUsuario) {
          const deliveryMarker = L.marker([
            entrega.ubicacionUsuario.latitude,
            entrega.ubicacionUsuario.longitude
          ])
              .addTo(map1)
              .bindPopup(`<b>${entrega.nombreUsuario}</b><br>Distancia: ${entrega.distanciaEntrega.toFixed(2)} metros`)

          markers1.push(deliveryMarker)
        }
      })

      // Add circle for max distance
      if (entregasCercanas.value.length > 0) {
        const maxDistance = Math.max(...entregasCercanas.value.map(e => e.distanciaEntrega))
        const circle = L.circle([farmaciaLat, farmaciaLng], {
          radius: maxDistance,
          color: 'blue',
          fillColor: '#30f',
          fillOpacity: 0.1
        }).addTo(map1)
        markers1.push(circle)
      }

      // Center map on farmacia
      map1.setView([farmaciaLat, farmaciaLng], 14)
    }
  } catch (e) {
    console.error('Error fetching entregas cercanas:', e)
    entregasCercanas.value = []
  }
})

async function checkZonaCobertura() {
  if (!clienteId.value) {
    coberturaError.value = 'Por favor ingrese un ID de cliente'
    return
  }

  try {
    zonaCobertura.value = await userService.getZonaCoberturaByClienteId(clienteId.value)
    coberturaError.value = null

    if (map2 && zonaCobertura.value) {
      clearMapLayers(map2, markers2)

      // Convert string coordinates to numbers
      const lat = parseFloat(zonaCobertura.value.latitud)
      const lng = parseFloat(zonaCobertura.value.longitud)

      if (!isNaN(lat) && !isNaN(lng)) {
        const marker = L.marker([lat, lng])
            .addTo(map2)
            .bindPopup(`<b>${zonaCobertura.value.nombre} ${zonaCobertura.value.apellido}</b><br>Sector: ${zonaCobertura.value.nombreSector}`)
            .openPopup()

        markers2.push(marker)
        map2.setView([lat, lng], 15)
      } else {
        coberturaError.value = 'Coordenadas inválidas para el cliente'
      }
    }
  } catch (e: any) {
    zonaCobertura.value = null
    coberturaError.value = e.response?.data?.message || 'Cliente no encontrado'
  }
}

async function fetchClientesLejanos() {
  try {
    clientesLejanos.value = await userService.getClientesLejanosDeFarmacia(radiusKm.value)
    console.log('Clientes lejanos:', clientesLejanos.value)

    if (map3 && clientesLejanos.value.length > 0) {
      clearMapLayers(map3, markers3)
      const farmaciaMap = new Map()
      clientesLejanos.value.forEach(cliente => {
        if (cliente.farmacia) {
          const farmaciaId = cliente.farmacia.idFarmacia
          if (!farmaciaMap.has(farmaciaId)) {
            farmaciaMap.set(farmaciaId, {
              farmacia: cliente.farmacia,
              clientes: []
            })
          }
          farmaciaMap.get(farmaciaId).clientes.push(cliente)
        }
      })

      // First, draw all circles (so they appear behind markers)
      farmaciaMap.forEach(({ farmacia, clientes }) => {
        if (farmacia.ubicacion) {
          // Check if coordinates are in the correct format
          const farmaciaLat = farmacia.ubicacion.latitude || farmacia.ubicacion.y
          const farmaciaLng = farmacia.ubicacion.longitude || farmacia.ubicacion.x

          console.log('Drawing circle for farmacia:', farmacia.nombreFarmacia, 'at', farmaciaLat, farmaciaLng)

          // Add green circle for farmacia coverage area
          const circle = L.circle([farmaciaLat, farmaciaLng], {
            radius: radiusKm.value * 1000,
            color: 'green',
            fillColor: '#0f0',
            fillOpacity: 0.1,
            weight: 2
          }).addTo(map3)

          markers3.push(circle)
        }
      })

      // Then draw lines (so they appear above circles but below markers)
      clientesLejanos.value.forEach((cliente, index) => {
        if (cliente.ubicacionCliente && cliente.farmacia?.ubicacion) {
          // Handle both coordinate formats
          const clienteLat = cliente.ubicacionCliente.latitude || cliente.ubicacionCliente.y
          const clienteLng = cliente.ubicacionCliente.longitude || cliente.ubicacionCliente.x
          const farmaciaLat = cliente.farmacia.ubicacion.latitude || cliente.farmacia.ubicacion.y
          const farmaciaLng = cliente.farmacia.ubicacion.longitude || cliente.farmacia.ubicacion.x

          console.log(`Drawing line ${index}:`,
              'Cliente:', clienteLat, clienteLng,
              'Farmacia:', farmaciaLat, farmaciaLng)

          // Draw thick orange line
          const line = L.polyline([
            [clienteLat, clienteLng],
            [farmaciaLat, farmaciaLng]
          ], {
            color: '#ff6600',
            weight: 3,
            opacity: 0.7,
            dashArray: '10, 5'
          }).addTo(map3)

          markers3.push(line)
        } else {
          console.log(`No line for cliente ${index}:`,
              'Cliente location:', cliente.ubicacionCliente,
              'Farmacia location:', cliente.farmacia?.ubicacion)
        }
      })

      // Draw farmacia markers
      farmaciaMap.forEach(({ farmacia, clientes }) => {
        if (farmacia.ubicacion) {
          const farmaciaLat = farmacia.ubicacion.latitude || farmacia.ubicacion.y
          const farmaciaLng = farmacia.ubicacion.longitude || farmacia.ubicacion.x

          // Add farmacia marker with white background and red cross
          const farmaciaIcon = L.divIcon({
            html: '<div style="background: white; border: 3px solid red; border-radius: 50%; width: 30px; height: 30px; display: flex; align-items: center; justify-content: center; font-weight: bold; color: red; font-size: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.3);">+</div>',
            iconSize: [30, 30],
            className: 'farmacia-icon'
          })

          const farmaciaMarker = L.marker([farmaciaLat, farmaciaLng], { icon: farmaciaIcon })
              .addTo(map3)
              .bindPopup(`<b>${farmacia.nombreFarmacia}</b><br>${farmacia.direccion}`)

          markers3.push(farmaciaMarker)
        }
      })

      // Finally, draw client markers (so they appear on top)
      clientesLejanos.value.forEach(cliente => {
        if (cliente.ubicacionCliente) {
          const clienteLat = cliente.ubicacionCliente.latitude || cliente.ubicacionCliente.y
          const clienteLng = cliente.ubicacionCliente.longitude || cliente.ubicacionCliente.x

          // Orange marker for clients outside radius
          const clienteIcon = L.divIcon({
            html: '<div style="background: #ff8800; border: 2px solid #cc6600; border-radius: 50%; width: 24px; height: 24px; box-shadow: 0 2px 4px rgba(0,0,0,0.3);"></div>',
            iconSize: [24, 24],
            className: 'cliente-lejano-icon'
          })

          const clienteMarker = L.marker([clienteLat, clienteLng], { icon: clienteIcon })
              .addTo(map3)
              .bindPopup(`<b>${cliente.nombreCliente}</b><br>A ${cliente.distanciaKm.toFixed(2)} km de ${cliente.farmacia?.nombreFarmacia || 'farmacia'}`)

          markers3.push(clienteMarker)
        }
      })

      // Adjust map view to show all markers
      const allLatLngs = []
      clientesLejanos.value.forEach(cliente => {
        if (cliente.ubicacionCliente) {
          const lat = cliente.ubicacionCliente.latitude || cliente.ubicacionCliente.y
          const lng = cliente.ubicacionCliente.longitude || cliente.ubicacionCliente.x
          allLatLngs.push([lat, lng])
        }
        if (cliente.farmacia?.ubicacion) {
          const lat = cliente.farmacia.ubicacion.latitude || cliente.farmacia.ubicacion.y
          const lng = cliente.farmacia.ubicacion.longitude || cliente.farmacia.ubicacion.x
          allLatLngs.push([lat, lng])
        }
      })

      if (allLatLngs.length > 0) {
        const bounds = L.latLngBounds(allLatLngs)
        map3.fitBounds(bounds, { padding: [50, 50] })
      }
    } else if (map3) {
      clearMapLayers(map3, markers3)
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
        <TabsTrigger value="segundo" class="rounded-sm">Zona Cobertura</TabsTrigger>
        <TabsTrigger value="tercero" class="rounded-sm">Clientes Lejanos</TabsTrigger>
      </TabsList>

      <TabsContent value="primero" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium mb-2">Seleccione Farmacia:</label>
            <select
                v-model.number="selectedFarmaciaId"
                class="w-full md:w-auto border rounded px-3 py-1.5 bg-white focus:outline-none focus:ring-2 focus:ring-primary"
            >
              <option :value="null" disabled>Seleccione una farmacia</option>
              <option v-for="f in farmacias" :key="f.idFarmacia" :value="f.idFarmacia">
                {{ f.nombreFarmacia }}
              </option>
            </select>
          </div>

          <div
              ref="map1Container"
              class="leaflet-container h-96 w-full rounded-lg overflow-hidden border"
          />

          <div v-if="entregasCercanas.length" class="mt-4">
            <h3 class="font-semibold mb-2">Entregas Cercanas ({{ entregasCercanas.length }}):</h3>
            <div class="max-h-40 overflow-y-auto">
              <ul class="list-disc ml-5 space-y-1">
                <li v-for="(e, idx) in entregasCercanas" :key="idx" class="text-sm">
                  <span class="font-medium">{{ e.nombreUsuario }}</span> -
                  <span class="text-muted-foreground">{{ e.distanciaEntrega.toFixed(2) }} metros</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </TabsContent>

      <TabsContent value="segundo" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <div class="space-y-4">
          <div class="flex items-center space-x-3">
            <input
                type="number"
                v-model.number="clienteId"
                placeholder="ID Cliente"
                class="border rounded px-3 py-1.5 w-32 focus:outline-none focus:ring-2 focus:ring-primary"
                @keyup.enter="checkZonaCobertura"
            />
            <button
                class="bg-primary text-primary-foreground px-4 py-1.5 rounded hover:bg-primary/90 transition-colors"
                @click="checkZonaCobertura"
            >
              Buscar Cliente
            </button>
          </div>

          <div ref="map2Container" class="h-96 w-full rounded-lg overflow-hidden border" />

          <div v-if="zonaCobertura" class="p-4 bg-green-50 border border-green-200 rounded-md">
            <h4 class="font-semibold text-green-800 mb-1">Cliente Encontrado</h4>
            <p class="text-sm text-green-700">
              <span class="font-medium">Nombre:</span> {{ zonaCobertura.nombre }} {{ zonaCobertura.apellido }}
            </p>
            <p class="text-sm text-green-700">
              <span class="font-medium">Sector:</span> {{ zonaCobertura.nombreSector }}
            </p>
            <p class="text-sm text-green-700">
              <span class="font-medium">Coordenadas:</span> {{ zonaCobertura.latitud }}, {{ zonaCobertura.longitud }}
            </p>
          </div>

          <div v-else-if="coberturaError" class="p-4 bg-red-50 border border-red-200 rounded-md">
            <p class="text-sm text-red-600">{{ coberturaError }}</p>
          </div>
        </div>
      </TabsContent>

      <TabsContent value="tercero" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <div class="space-y-4">
          <div class="flex items-center space-x-3">
            <label class="text-sm font-medium">Radio (km):</label>
            <input
                type="number"
                v-model.number="radiusKm"
                class="border rounded px-3 py-1.5 w-20 focus:outline-none focus:ring-2 focus:ring-primary"
                min="1"
                max="50"
            />
            <button
                class="bg-primary text-primary-foreground px-4 py-1.5 rounded hover:bg-primary/90 transition-colors"
                @click="fetchClientesLejanos"
            >
              Buscar Clientes Lejanos
            </button>
          </div>

          <div ref="map3Container" class="h-96 w-full rounded-lg overflow-hidden border" />

          <div v-if="clientesLejanos.length" class="mt-4">
            <h3 class="font-semibold mb-2">
              Clientes fuera del radio ({{ clientesLejanos.length }} encontrados):
            </h3>
            <div class="max-h-60 overflow-y-auto">
              <ul class="list-disc ml-5 space-y-1">
                <li v-for="c in clientesLejanos" :key="c.idCliente" class="text-sm">
                  <span class="font-medium">{{ c.nombreCliente }}</span> -
                  <span class="text-muted-foreground">
                    {{ c.distanciaKm.toFixed(2) }} km desde {{ c.farmacia.nombreFarmacia }}
                  </span>
                </li>
              </ul>
            </div>
          </div>

          <div v-else-if="clientesLejanos.length === 0 && radiusKm" class="p-4 bg-blue-50 border border-blue-200 rounded-md">
            <p class="text-sm text-blue-600">
              No se encontraron clientes fuera del radio de {{ radiusKm }} km
            </p>
          </div>
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
  background: #f3f4f6;
}

/* Ensure the map container has explicit height */
.leaflet-container {
  min-height: 384px; /* h-96 = 24rem = 384px */
}

:deep(.leaflet-control-container) {
  position: absolute;
  pointer-events: none;
}

:deep(.leaflet-control) {
  pointer-events: auto;
}

:deep(.leaflet-popup-close-button) {
  color: #333;
  font-size: 20px;
  font-weight: bold;
}

/* Fix potential z-index issues */
:deep(.leaflet-pane) {
  z-index: 400;
}

:deep(.leaflet-tile-pane) {
  z-index: 200;
}

:deep(.leaflet-overlay-pane) {
  z-index: 400;
}

:deep(.leaflet-shadow-pane) {
  z-index: 500;
}

:deep(.leaflet-marker-pane) {
  z-index: 600;
}

:deep(.leaflet-tooltip-pane) {
  z-index: 650;
}

:deep(.leaflet-popup-pane) {
  z-index: 700;
}
</style>