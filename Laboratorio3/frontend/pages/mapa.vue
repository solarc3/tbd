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
import { getRutasFrecuentes } from '@/api/services/repartidorService'
import 'leaflet/dist/leaflet.css'
import type {
  FarmaciaEntity,
  FarmaciaClosestDeliveryResponse,
  ClienteZonaCobertura,
  ClienteLejanoDeFarmacia,
  UserInfoResponse,
  RepartidorRutasFrecuentesDTO,
} from '@/api/models'

const farmacias = ref<FarmaciaEntity[]>([])
const selectedFarmaciaId = ref<number | null>(null)
const entregasCercanas = ref<FarmaciaClosestDeliveryResponse[]>([])

// Variables para usuarios y zona de cobertura
const allUsers = ref<UserInfoResponse[]>([])
const selectedUserId = ref<number | null>(null)
const clienteId = ref<number | null>(null)
const zonaCobertura = ref<ClienteZonaCobertura | null>(null)
const coberturaError = ref<string | null>(null)

const radiusKm = ref(5)
const clientesLejanos = ref<ClienteLejanoDeFarmacia[]>([])

// Variables para rutas frecuentes
const repartidoresRutas = ref<RepartidorRutasFrecuentesDTO[]>([])
const selectedRepartidorId = ref<number | null>(null)
const rutasError = ref<string | null>(null)

const map1Container = ref<HTMLElement | null>(null)
const map2Container = ref<HTMLElement | null>(null)
const map3Container = ref<HTMLElement | null>(null)
const map4Container = ref<HTMLElement | null>(null)
const activeTab = ref('primero')

let L: any;
let leafletPromise: Promise<any> | null = null;
let map1: any = null
let map2: any = null
let map3: any = null
let map4: any = null
let markers1: any[] = []
let markers2: any[] = []
let markers3: any[] = []
let markers4: any[] = []

function loadLeaflet() {
  if (typeof window === 'undefined') {
    return Promise.reject("Not in a client environment");
  }
  if (!leafletPromise) {
    leafletPromise = import('leaflet').then(leafletModule => {
      L = leafletModule.default;
      delete (L.Icon.Default.prototype as any)._getIconUrl;
      L.Icon.Default.mergeOptions({
        iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon-2x.png',
        iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
      });
      return L;
    });
  }
  return leafletPromise;
}

function createFarmaciaIcon() {
  return L.divIcon({
    html: `<div style="
      background: white;
      border: 2px solid #dc2626;
      border-radius: 8px;
      width: 36px;
      height: 36px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #dc2626;
      box-shadow: 0 2px 6px rgba(0,0,0,0.3);
      cursor: pointer;
      position: relative;
      z-index: 1000;
    ">
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-hospital-icon lucide-hospital"><path d="M12 6v4"/><path d="M14 14h-4"/><path d="M14 18h-4"/><path d="M14 8h-4"/><path d="M18 12h2a2 2 0 0 1 2 2v6a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2v-9a2 2 0 0 1 2-2h2"/><path d="M18 22V4a2 2 0 0 0-2-2H8a2 2 0 0 0-2 2v18"/></svg>
    </div>`,
    iconSize: [36, 36],
    iconAnchor: [18, 18],
    popupAnchor: [0, -18],
    className: 'farmacia-custom-icon'
  })
}

function createUserIcon() {
  return L.divIcon({
    html: `<div style="
      background: #3b82f6;
      border: 2px solid #1d4ed8;
      border-radius: 8px;
      width: 32px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      box-shadow: 0 2px 6px rgba(0,0,0,0.3);
      cursor: pointer;
      position: relative;
      z-index: 1000;
    ">
      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
        <circle cx="12" cy="7" r="4"/>
      </svg>
    </div>`,
    iconSize: [32, 32],
    iconAnchor: [16, 16],
    popupAnchor: [0, -16],
    className: 'user-custom-icon'
  })
}

function createDeliveryIcon() {
  return L.divIcon({
    html: `<div style="
      background: #10b981;
      border: 2px solid #059669;
      border-radius: 8px;
      width: 32px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      box-shadow: 0 2px 6px rgba(0,0,0,0.3);
      cursor: pointer;
      position: relative;
      z-index: 1000;
    ">
      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
      </svg>
    </div>`,
    iconSize: [32, 32],
    iconAnchor: [16, 16],
    popupAnchor: [0, -16],
    className: 'delivery-custom-icon'
  })
}

async function initializeMap(container: HTMLElement, view: [number, number], zoom: number): Promise<any> {
  try {
    await loadLeaflet();
  } catch (error) {
    console.error("Failed to load Leaflet:", error);
    return null;
  }

  const map = L.map(container, {
    zoomControl: true,
    attributionControl: true
  }).setView(view, zoom)

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 19
  }).addTo(map)

  return map
}

function clearMapLayers(map: any, markers: any[]) {
  if (!map) return
  markers.forEach(layer => {
    if (map.hasLayer(layer)) {
      map.removeLayer(layer)
    }
  })
  markers.length = 0
}

onMounted(async () => {
  try {
    farmacias.value = await farmaciaService.getAllFarmacias()
    allUsers.value = await userService.getAllUsers()
    repartidoresRutas.value = await getRutasFrecuentes()
  } catch (error) {
    console.error('Error fetching data:', error)
  }

  await nextTick()
  if (activeTab.value === 'primero' && map1Container.value) {
    map1 = await initializeMap(map1Container.value, [-33.45, -70.65], 13)
  }
})

watch(activeTab, async (newTab, oldTab) => {
  if (oldTab) {
    switch (oldTab) {
      case 'primero':
        if (map1) {
          clearMapLayers(map1, markers1)
          map1.remove();
          map1 = null;
        }
        break
      case 'segundo':
        if (map2) {
          clearMapLayers(map2, markers2)
          map2.remove();
          map2 = null;
        }
        break
      case 'tercero':
        if (map3) {
          clearMapLayers(map3, markers3)
          map3.remove();
          map3 = null;
        }
        break
      case 'cuarto':
        if (map4) {
          clearMapLayers(map4, markers4)
          map4.remove();
          map4 = null;
        }
        break
    }
  }

  await nextTick()

  setTimeout(async () => {
    if (newTab === 'primero' && !map1 && map1Container.value) {
      map1 = await initializeMap(map1Container.value, [-33.45, -70.65], 13)
      if(selectedFarmaciaId.value) renderEntregasCercanas();
    } else if (newTab === 'segundo' && !map2 && map2Container.value) {
      map2 = await initializeMap(map2Container.value, [-33.45, -70.65], 13)
      if(selectedUserId.value) checkZonaCobertura();
    } else if (newTab === 'tercero' && !map3 && map3Container.value) {
      map3 = await initializeMap(map3Container.value, [-33.45, -70.65], 11)
      if(clientesLejanos.value.length > 0) fetchClientesLejanos();
    } else if (newTab === 'cuarto' && !map4 && map4Container.value) {
      map4 = await initializeMap(map4Container.value, [-33.45, -70.65], 12)
      if(selectedRepartidorId.value) renderRutasFrecuentes();
    }
  }, 100)
})

watch(selectedFarmaciaId, async (id) => {
  if (id == null) return;
  try {
    entregasCercanas.value = await farmaciaService.getEntregasCercanas(id);
    renderEntregasCercanas();
  } catch (e) {
    console.error('Error fetching entregas cercanas:', e)
    entregasCercanas.value = []
  }
})

watch(selectedUserId, async (userId) => {
  if (userId == null) {
    zonaCobertura.value = null;
    coberturaError.value = null;
    clearMapLayers(map2, markers2);
    return;
  }
  clienteId.value = userId;
  await checkZonaCobertura();
})

watch(selectedRepartidorId, () => {
  if (selectedRepartidorId.value && map4) {
    renderRutasFrecuentes();
  }
})

async function renderEntregasCercanas() {
  if (!map1 || selectedFarmaciaId.value == null) return;

  const farmacia = farmacias.value.find(f => f.idFarmacia === selectedFarmaciaId.value);
  if (!farmacia) return;

  clearMapLayers(map1, markers1);

  let farmaciaLat, farmaciaLng;
  if (entregasCercanas.value.length > 0 && entregasCercanas.value[0].ubicacionFarmacia) {
    farmaciaLat = entregasCercanas.value[0].ubicacionFarmacia.latitude;
    farmaciaLng = entregasCercanas.value[0].ubicacionFarmacia.longitude;
  } else if (farmacia.ubicacion) {
    farmaciaLat = farmacia.ubicacion.y;
    farmaciaLng = farmacia.ubicacion.x;
  } else {
    console.error('No se encontraron coordenadas para la farmacia');
    return;
  }

  if (entregasCercanas.value.length > 0) {
    const maxDistance = Math.max(...entregasCercanas.value.map(e => e.distanciaEntrega));
    const circle = L.circle([farmaciaLat, farmaciaLng], {
      radius: maxDistance,
      color: 'green',
      fillColor: '#0f0',
      fillOpacity: 0.1,
      weight: 2
    }).addTo(map1);
    markers1.push(circle);

    const entregaMasLejana = entregasCercanas.value.reduce((max, entrega) =>
        entrega.distanciaEntrega > max.distanciaEntrega ? entrega : max
    );

    if (entregaMasLejana.ubicacionUsuario) {
      const line = L.polyline([
        [farmaciaLat, farmaciaLng],
        [entregaMasLejana.ubicacionUsuario.latitude, entregaMasLejana.ubicacionUsuario.longitude]
      ], {
        color: '#ff6600',
        weight: 3,
        opacity: 0.8,
        dashArray: '10, 5'
      }).addTo(map1);
      markers1.push(line);
    }
  }

  const farmaciaMarker = L.marker([farmaciaLat, farmaciaLng], {
    icon: createFarmaciaIcon(),
    zIndexOffset: 1000
  })
      .addTo(map1)
      .bindPopup(`<b>${farmacia.nombreFarmacia}</b><br>${farmacia.direccion}`)
      .openPopup();
  markers1.push(farmaciaMarker);

  const distanciaMaxima = entregasCercanas.value.length > 0
      ? Math.max(...entregasCercanas.value.map(e => e.distanciaEntrega))
      : 0;

  entregasCercanas.value.forEach((entrega) => {
    if (entrega.ubicacionUsuario) {
      const esElMasLejano = entrega.distanciaEntrega === distanciaMaxima;
      const deliveryMarker = L.marker([
        entrega.ubicacionUsuario.latitude,
        entrega.ubicacionUsuario.longitude
      ], {
        icon: createUserIcon(),
        zIndexOffset: esElMasLejano ? 600 : 500
      })
          .addTo(map1)
          .bindPopup(`<b>${entrega.nombreUsuario}</b><br>Distancia: ${entrega.distanciaEntrega.toFixed(2)} metros${esElMasLejano ? '<br><span style="color: #ff6600; font-weight: bold;">⚠️ Más lejano</span>' : ''}`);
      markers1.push(deliveryMarker);
    }
  });

  map1.setView([farmaciaLat, farmaciaLng], 14);
}

async function checkZonaCobertura() {
  if (!clienteId.value) {
    coberturaError.value = 'Por favor seleccione un cliente';
    return;
  }
  if (!map2) {
    coberturaError.value = 'El mapa no está inicializado. Intente de nuevo.';
    return;
  }

  try {
    zonaCobertura.value = await userService.getZonaCoberturaByClienteId(clienteId.value);
    coberturaError.value = null;

    clearMapLayers(map2, markers2);

    const lat = parseFloat(zonaCobertura.value.latitud);
    const lng = parseFloat(zonaCobertura.value.longitud);

    if (!isNaN(lat) && !isNaN(lng)) {
      const circle = L.circle([lat, lng], {
        radius: 1000,
        color: 'blue',
        fillColor: '#30f',
        fillOpacity: 0.1,
        weight: 2
      }).addTo(map2);
      markers2.push(circle);

      const marker = L.marker([lat, lng], {
        icon: createUserIcon(),
        zIndexOffset: 1000
      })
          .addTo(map2)
          .bindPopup(`<b>${zonaCobertura.value.nombre} ${zonaCobertura.value.apellido}</b><br>Sector: ${zonaCobertura.value.nombreSector}`)
          .openPopup();
      markers2.push(marker);

      map2.setView([lat, lng], 15);

    } else {
      coberturaError.value = 'Coordenadas inválidas para el cliente';
    }

  } catch (e: any) {
    zonaCobertura.value = null;
    clearMapLayers(map2, markers2);
    coberturaError.value = e.response?.data?.message || 'Cliente no encontrado';
  }
}

async function fetchClientesLejanos() {
  if (!map3) return;

  try {
    clientesLejanos.value = await userService.getClientesLejanosDeFarmacia(radiusKm.value);
    clearMapLayers(map3, markers3);

    if (clientesLejanos.value.length === 0) {
      return;
    }

    const farmaciaMap = new Map();
    clientesLejanos.value.forEach(cliente => {
      if (cliente.farmacia) {
        const farmaciaId = cliente.farmacia.idFarmacia;
        if (!farmaciaMap.has(farmaciaId)) {
          farmaciaMap.set(farmaciaId, {
            farmacia: cliente.farmacia,
            clientes: []
          });
        }
        farmaciaMap.get(farmaciaId).clientes.push(cliente);
      }
    });

    farmaciaMap.forEach(({ farmacia }) => {
      if (farmacia.ubicacion) {
        const farmaciaLat = farmacia.ubicacion.latitude || farmacia.ubicacion.y;
        const farmaciaLng = farmacia.ubicacion.longitude || farmacia.ubicacion.x;
        const circle = L.circle([farmaciaLat, farmaciaLng], {
          radius: radiusKm.value * 1000,
          color: 'green',
          fillColor: '#0f0',
          fillOpacity: 0.1,
          weight: 2
        }).addTo(map3);
        markers3.push(circle);
      }
    });

    clientesLejanos.value.forEach(cliente => {
      if (cliente.ubicacionCliente && cliente.farmacia?.ubicacion) {
        const clienteLat = cliente.ubicacionCliente.latitude || cliente.ubicacionCliente.y;
        const clienteLng = cliente.ubicacionCliente.longitude || cliente.ubicacionCliente.x;
        const farmaciaLat = cliente.farmacia.ubicacion.latitude || cliente.farmacia.ubicacion.y;
        const farmaciaLng = cliente.farmacia.ubicacion.longitude || cliente.farmacia.ubicacion.x;

        const line = L.polyline([[clienteLat, clienteLng], [farmaciaLat, farmaciaLng]], {
          color: '#ff6600',
          weight: 3,
          opacity: 0.7,
          dashArray: '10, 5'
        }).addTo(map3);
        markers3.push(line);
      }
    });

    clientesLejanos.value.forEach(cliente => {
      if (cliente.ubicacionCliente) {
        const clienteLat = cliente.ubicacionCliente.latitude || cliente.ubicacionCliente.y;
        const clienteLng = cliente.ubicacionCliente.longitude || cliente.ubicacionCliente.x;

        const clienteMarker = L.marker([clienteLat, clienteLng], {
          icon: createUserIcon(),
          zIndexOffset: 500
        })
            .addTo(map3)
            .bindPopup(`<b>${cliente.nombreCliente}</b><br>A ${cliente.distanciaKm.toFixed(2)} km`);
        markers3.push(clienteMarker);
      }
    });

    farmaciaMap.forEach(({ farmacia }) => {
      if (farmacia.ubicacion) {
        const farmaciaLat = farmacia.ubicacion.latitude || farmacia.ubicacion.y;
        const farmaciaLng = farmacia.ubicacion.longitude || farmacia.ubicacion.x;

        const farmaciaMarker = L.marker([farmaciaLat, farmaciaLng], {
          icon: createFarmaciaIcon(),
          zIndexOffset: 1000
        })
            .addTo(map3)
            .bindPopup(`<b>${farmacia.nombreFarmacia}</b><br>${farmacia.direccion}`);
        markers3.push(farmaciaMarker);
      }
    });

    const allLatLngs = clientesLejanos.value.flatMap(c => {
      const points = [];
      if(c.ubicacionCliente) points.push([c.ubicacionCliente.latitude || c.ubicacionCliente.y, c.ubicacionCliente.longitude || c.ubicacionCliente.x]);
      if(c.farmacia?.ubicacion) points.push([c.farmacia.ubicacion.latitude || c.farmacia.ubicacion.y, c.farmacia.ubicacion.longitude || c.farmacia.ubicacion.x]);
      return points;
    });

    if (allLatLngs.length > 0) {
      const bounds = L.latLngBounds(allLatLngs);
      map3.fitBounds(bounds, { padding: [50, 50] });
    }

  } catch (e) {
    console.error('Error fetching clientes lejanos:', e);
    clientesLejanos.value = [];
  }
}

async function renderRutasFrecuentes() {
  if (!map4 || !selectedRepartidorId.value) return;

  clearMapLayers(map4, markers4);
  rutasError.value = null;

  const repartidor = repartidoresRutas.value.find(r => r.idRepartidor === selectedRepartidorId.value);
  if (!repartidor || !repartidor.rutasFrecuentes || repartidor.rutasFrecuentes.length === 0) {
    rutasError.value = 'No hay rutas frecuentes para este repartidor';
    return;
  }

  // Encontrar la frecuencia máxima para normalizar el tamaño de los círculos
  const maxFrecuencia = Math.max(...repartidor.rutasFrecuentes.map(r => r.frecuencia));
  const minFrecuencia = Math.min(...repartidor.rutasFrecuentes.map(r => r.frecuencia));

  // Crear un heatmap layer con los puntos
  const heatmapPoints: any[] = [];

  // Dibujar círculos para cada punto frecuente
  repartidor.rutasFrecuentes.forEach((punto) => {
    // Normalizar el radio basado en la frecuencia
    const radioBase = 100;
    const radioMax = 500;
    const factorNormalizacion = (punto.frecuencia - minFrecuencia) / (maxFrecuencia - minFrecuencia || 1);
    const radio = radioBase + (radioMax - radioBase) * factorNormalizacion;

    // Color basado en frecuencia
    const opacidad = 0.3 + 0.4 * factorNormalizacion;
    const color = getColorByFrequency(factorNormalizacion);

    const circle = L.circle([punto.lat, punto.lng], {
      radius: radio,
      color: color,
      fillColor: color,
      fillOpacity: opacidad,
      weight: 2
    }).addTo(map4);

    circle.bindPopup(`
      <b>Punto Frecuente</b><br>
      Frecuencia: ${punto.frecuencia} veces<br>
      Coordenadas: ${punto.lat.toFixed(4)}, ${punto.lng.toFixed(4)}
    `);

    markers4.push(circle);

    // Agregar puntos para el heatmap
    for (let i = 0; i < punto.frecuencia; i++) {
      heatmapPoints.push([punto.lat, punto.lng]);
    }
  });

  // Dibujar líneas conectando los puntos más frecuentes
  const puntosFrecuentes = repartidor.rutasFrecuentes
      .filter(p => p.frecuencia >= maxFrecuencia * 0.5)
      .sort((a, b) => b.frecuencia - a.frecuencia);

  if (puntosFrecuentes.length >= 2) {
    // Conectar los puntos frecuentes en orden
    const coordinates = puntosFrecuentes.map(p => [p.lat, p.lng]);
    const polyline = L.polyline(coordinates, {
      color: '#6366f1',
      weight: 3,
      opacity: 0.6,
      dashArray: '10, 5'
    }).addTo(map4);
    markers4.push(polyline);
  }

  // Agregar marcadores especiales para los puntos más frecuentes
  const topPuntos = repartidor.rutasFrecuentes
      .sort((a, b) => b.frecuencia - a.frecuencia)
      .slice(0, 5);

  topPuntos.forEach((punto, index) => {
    const marker = L.marker([punto.lat, punto.lng], {
      icon: createDeliveryIcon(),
      zIndexOffset: 1000 - index
    })
        .addTo(map4)
        .bindPopup(`
        <b>Top ${index + 1} Punto Más Frecuente</b><br>
        <span style="color: #10b981; font-weight: bold;">
        Frecuencia: ${punto.frecuencia} veces
        </span><br>
        Coordenadas: ${punto.lat.toFixed(4)}, ${punto.lng.toFixed(4)}
      `);
    markers4.push(marker);
  });

  // Centrar el mapa en todos los puntos
  if (repartidor.rutasFrecuentes.length > 0) {
    const bounds = L.latLngBounds(
        repartidor.rutasFrecuentes.map(p => [p.lat, p.lng])
    );
    map4.fitBounds(bounds, { padding: [50, 50] });
  }
}

function getColorByFrequency(factor: number): string {
  // Gradiente de colores de menos frecuente (azul) a más frecuente (rojo)
  if (factor < 0.25) return '#3b82f6'; // Azul
  if (factor < 0.5) return '#10b981';  // Verde
  if (factor < 0.75) return '#f59e0b'; // Naranja
  return '#ef4444'; // Rojo
}
</script>

<template>
  <div class="w-full p-6">
    <Tabs v-model="activeTab" default-value="primero" class="w-full">
      <TabsList class="grid w-full grid-cols-4 h-10 bg-muted p-1 rounded-md">
        <TabsTrigger value="primero" class="rounded-sm">Farmacias</TabsTrigger>
        <TabsTrigger value="segundo" class="rounded-sm">Zona Cobertura</TabsTrigger>
        <TabsTrigger value="tercero" class="rounded-sm">Clientes Lejanos</TabsTrigger>
        <TabsTrigger value="cuarto" class="rounded-sm">Rutas Frecuentes</TabsTrigger>
      </TabsList>

      <TabsContent value="primero" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <div class="space-y-4">
          <div class="p-4 bg-muted/30 border rounded-lg">
            <label class="block text-sm font-medium mb-2">Seleccione Farmacia:</label>
            <select
                v-model.number="selectedFarmaciaId"
                class="w-full border rounded px-3 py-1.5 bg-white focus:outline-none focus:ring-2 focus:ring-primary"
            >
              <option :value="null" disabled>Seleccione una farmacia</option>
              <option v-for="f in farmacias" :key="f.idFarmacia" :value="f.idFarmacia">
                {{ f.nombreFarmacia }}
              </option>
            </select>
          </div>

          <div class="grid grid-cols-1 lg:grid-cols-4 gap-4">
            <div class="lg:col-span-1">
              <div v-if="entregasCercanas.length" class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Entregas Cercanas</h3>
                <div class="bg-muted/30 rounded-lg p-3 mb-3">
                  <p class="text-sm font-medium text-muted-foreground">Total entregas</p>
                  <p class="text-2xl font-bold">{{ entregasCercanas.length }}</p>
                </div>
                <div class="space-y-2 max-h-[300px] overflow-y-auto pr-2">
                  <div
                      v-for="(e, idx) in entregasCercanas"
                      :key="idx"
                      class="p-3 bg-background border rounded-lg hover:bg-muted/20 transition-colors"
                      :class="{ 'border-orange-500 bg-orange-50': e.distanciaEntrega === Math.max(...entregasCercanas.map(ent => ent.distanciaEntrega)) }"
                  >
                    <p class="font-medium text-sm">{{ e.nombreUsuario }}</p>
                    <p class="text-sm text-muted-foreground">{{ e.distanciaEntrega.toFixed(0) }} m</p>
                    <div v-if="e.distanciaEntrega === Math.max(...entregasCercanas.map(ent => ent.distanciaEntrega))"
                         class="mt-1">
                      <span class="text-xs bg-orange-500 text-white px-2 py-0.5 rounded-full">Más lejano</span>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else-if="selectedFarmaciaId" class="text-center py-8">
                <p class="text-muted-foreground">No hay entregas cercanas</p>
              </div>
            </div>

            <div class="lg:col-span-3">
              <div
                  ref="map1Container"
                  class="leaflet-container h-[400px] w-full rounded-lg overflow-hidden border"
              />
            </div>
          </div>
        </div>
      </TabsContent>

      <TabsContent value="segundo" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <div class="space-y-4">
          <div class="p-4 bg-muted/30 border rounded-lg">
            <label class="block text-sm font-medium mb-2">Seleccione Cliente:</label>
            <select
                v-model.number="selectedUserId"
                class="w-full border rounded px-3 py-1.5 bg-white focus:outline-none focus:ring-2 focus:ring-primary"
            >
              <option :value="null" disabled>Seleccione un cliente</option>
              <option v-for="user in allUsers" :key="user.id" :value="user.id">
                {{ user.firstName }} {{ user.lastName }} ({{ user.username }})
              </option>
            </select>
          </div>

          <div class="grid grid-cols-1 lg:grid-cols-4 gap-4">
            <div class="lg:col-span-1">
              <div v-if="zonaCobertura" class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Información del Cliente</h3>
                <div class="p-4 bg-green-50 border border-green-200 rounded-lg space-y-3">
                  <div>
                    <p class="text-xs font-medium text-green-600 uppercase tracking-wider">Nombre</p>
                    <p class="text-sm font-semibold text-green-800">{{ zonaCobertura.nombre }} {{ zonaCobertura.apellido }}</p>
                  </div>
                  <div>
                    <p class="text-xs font-medium text-green-600 uppercase tracking-wider">Sector</p>
                    <p class="text-sm font-semibold text-green-800">{{ zonaCobertura.nombreSector }}</p>
                  </div>
                  <div>
                    <p class="text-xs font-medium text-green-600 uppercase tracking-wider">Coordenadas</p>
                    <p class="text-sm font-semibold text-green-800">{{ parseFloat(zonaCobertura.latitud).toFixed(4) }}, {{ parseFloat(zonaCobertura.longitud).toFixed(4) }}</p>
                  </div>
                  <div class="pt-2 border-t border-green-200">
                    <div class="flex items-center space-x-2">
                      <div class="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
                      <p class="text-xs text-green-600">Dentro de cobertura</p>
                    </div>
                  </div>
                </div>
              </div>

              <div v-else-if="coberturaError" class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Estado de Búsqueda</h3>
                <div class="p-4 bg-red-50 border border-red-200 rounded-lg">
                  <p class="text-sm text-red-600">{{ coberturaError }}</p>
                </div>
              </div>

              <div v-else class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Búsqueda de Cliente</h3>
                <div class="p-4 bg-muted/30 rounded-lg">
                  <p class="text-sm text-muted-foreground">Seleccione un cliente para buscar su ubicación y zona de cobertura</p>
                </div>
              </div>
            </div>

            <div class="lg:col-span-3">
              <div ref="map2Container" class="h-[400px] w-full rounded-lg overflow-hidden border" />
            </div>
          </div>
        </div>
      </TabsContent>

      <TabsContent value="tercero" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <div class="space-y-4">
          <div class="p-4 bg-muted/30 border rounded-lg flex flex-wrap items-center gap-4">
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

          <div class="grid grid-cols-1 lg:grid-cols-4 gap-4">
            <div class="lg:col-span-1">
              <div v-if="clientesLejanos.length" class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Clientes Fuera del Radio</h3>
                <div class="bg-orange-50 border border-orange-200 rounded-lg p-3 mb-3">
                  <p class="text-sm font-medium text-orange-600">Total encontrados</p>
                  <p class="text-2xl font-bold text-orange-700">{{ clientesLejanos.length }}</p>
                  <p class="text-xs text-orange-600 mt-1">Radio: {{ radiusKm }} km</p>
                </div>
                <div class="space-y-2 max-h-[300px] overflow-y-auto pr-2">
                  <div
                      v-for="c in clientesLejanos"
                      :key="c.idCliente"
                      class="p-3 bg-background border rounded-lg hover:bg-muted/20 transition-colors"
                  >
                    <p class="font-medium text-sm">{{ c.nombreCliente }}</p>
                    <p class="text-xs text-muted-foreground">{{ c.farmacia.nombreFarmacia }}</p>
                    <div class="flex items-center justify-between mt-2">
                      <span class="text-sm font-semibold text-orange-600">{{ c.distanciaKm.toFixed(1) }} km</span>
                      <span class="text-xs bg-orange-100 text-orange-700 px-2 py-0.5 rounded-full">
                        +{{ (c.distanciaKm - radiusKm).toFixed(1) }} km
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div v-else class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Análisis de Cobertura</h3>
                <div class="p-4 bg-blue-50 border border-blue-200 rounded-lg">
                  <p class="text-sm text-blue-600">
                    No se encontraron clientes fuera del radio de {{ radiusKm }} km o aún no se ha realizado una búsqueda.
                  </p>
                  <p class="text-xs text-blue-500 mt-2">
                    Ajuste el radio y presione "Buscar" para analizar la cobertura.
                  </p>
                </div>
              </div>
            </div>

            <div class="lg:col-span-3">
              <div ref="map3Container" class="h-[400px] w-full rounded-lg overflow-hidden border" />
            </div>
          </div>
        </div>
      </TabsContent>

      <TabsContent value="cuarto" class="mt-6 p-6 bg-background rounded-lg border shadow-sm">
        <div class="space-y-4">
          <div class="p-4 bg-muted/30 border rounded-lg">
            <label class="block text-sm font-medium mb-2">Seleccione Repartidor:</label>
            <select
                v-model.number="selectedRepartidorId"
                class="w-full border rounded px-3 py-1.5 bg-white focus:outline-none focus:ring-2 focus:ring-primary"
            >
              <option :value="null" disabled>Seleccione un repartidor</option>
              <option v-for="rep in repartidoresRutas" :key="rep.idRepartidor" :value="rep.idRepartidor">
                {{ rep.nombreRepartidor }}
              </option>
            </select>
          </div>

          <div class="grid grid-cols-1 lg:grid-cols-4 gap-4">
            <div class="lg:col-span-1">
              <div v-if="selectedRepartidorId && !rutasError" class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Análisis de Rutas</h3>

                <div v-if="repartidoresRutas.find(r => r.idRepartidor === selectedRepartidorId)?.rutasFrecuentes?.length" class="space-y-3">
                  <div class="bg-green-50 border border-green-200 rounded-lg p-3">
                    <p class="text-sm font-medium text-green-600">Puntos frecuentes</p>
                    <p class="text-2xl font-bold text-green-700">
                      {{ repartidoresRutas.find(r => r.idRepartidor === selectedRepartidorId)?.rutasFrecuentes?.length || 0 }}
                    </p>
                  </div>

                  <div class="bg-blue-50 border border-blue-200 rounded-lg p-3">
                    <p class="text-sm font-medium text-blue-600">Frecuencia máxima</p>
                    <p class="text-2xl font-bold text-blue-700">
                      {{ Math.max(...(repartidoresRutas.find(r => r.idRepartidor === selectedRepartidorId)?.rutasFrecuentes?.map(r => r.frecuencia) || [0])) }}
                    </p>
                    <p class="text-xs text-blue-600 mt-1">veces en el mismo punto</p>
                  </div>

                  <div class="space-y-2">
                    <h4 class="font-medium text-sm text-muted-foreground">Top 5 Puntos Más Visitados</h4>
                    <div class="space-y-2 max-h-[250px] overflow-y-auto pr-2">
                      <div
                          v-for="(punto, idx) in repartidoresRutas.find(r => r.idRepartidor === selectedRepartidorId)?.rutasFrecuentes?.sort((a, b) => b.frecuencia - a.frecuencia).slice(0, 5)"
                          :key="idx"
                          class="p-3 bg-background border rounded-lg hover:bg-muted/20 transition-colors"
                      >
                        <div class="flex items-center justify-between">
                          <span class="text-sm font-medium">#{{ idx + 1 }}</span>
                          <span class="text-sm font-bold text-gray-600">{{ punto.frecuencia }} veces</span>
                        </div>
                        <p class="text-xs text-muted-foreground mt-1">
                          {{ punto.lat.toFixed(4) }}, {{ punto.lng.toFixed(4) }}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-else class="p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                  <p class="text-sm text-yellow-600">No hay datos de rutas para este repartidor</p>
                </div>
              </div>

              <div v-else-if="rutasError" class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Estado</h3>
                <div class="p-4 bg-red-50 border border-red-200 rounded-lg">
                  <p class="text-sm text-red-600">{{ rutasError }}</p>
                </div>
              </div>

              <div v-else class="sticky top-0">
                <h3 class="font-semibold mb-3 text-lg">Rutas de Repartidores</h3>
              </div>
            </div>

            <div class="lg:col-span-3">
              <div ref="map4Container" class="h-[400px] w-full rounded-lg overflow-hidden border" />
            </div>
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

.leaflet-container {
  min-height: 400px;
}

:deep(.leaflet-control-container) {
  position: absolute;
  pointer-events: none;
  z-index: 1000;
}

:deep(.leaflet-control) {
  pointer-events: auto;
}

:deep(.farmacia-custom-icon),
:deep(.user-custom-icon),
:deep(.delivery-custom-icon) {
  cursor: pointer !important;
  pointer-events: auto !important;
}

:deep(.leaflet-marker-icon) {
  cursor: pointer !important;
}

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

:deep(.leaflet-popup) {
  z-index: 1000;
}
</style>