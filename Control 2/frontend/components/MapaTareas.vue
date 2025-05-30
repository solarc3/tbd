<template>
  <Card class="h-full w-full !p-0">
    <CardContent class="p-0 h-full">
      <div ref="mapContainer" class="h-full w-full rounded-lg overflow-hidden"/>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick, watch } from 'vue';
import { Card, CardContent } from "@/components/ui/card";
import 'leaflet/dist/leaflet.css';
import SectorService, { type SectorEntity } from "@/api/services/sectorService";

const props = defineProps({
  initialLat: { type: Number, default: -33.45 },
  initialLng: { type: Number, default: -70.6667 },
  initialZoom: { type: Number, default: 13 },
  selectedLatitude: { type: Number as () => number | null, default: null },
  selectedLongitude: { type: Number as () => number | null, default: null }
});
const emit = defineEmits(['location-selected']);

const mapContainer = ref<HTMLElement | null>(null);
let map: any = null;
let selectionMarker: any = null;
let L_instance: any = null;
let sectorLayers: any[] = [];
const sectores = ref<SectorEntity[]>([]);
const loading = ref(true);

const sectorColors = [
  '#FF5733', '#33FF57', '#3357FF', '#F033FF', '#FF33A8',
  '#33FFF6', '#FFD133', '#8C33FF', '#FF8C33', '#33FFBD',
  '#BD33FF', '#33BDFF', '#FF3333', '#33FF33', '#3333FF',
  '#FF33FF', '#33FFFF', '#FFFF33', '#FF6633', '#33FF66'
];


const fetchSectores = async () => {
  try {
    loading.value = true;
    sectores.value = await SectorService.getAllSectores();
    console.log('Sectores cargados:', sectores.value);

    if (map && L_instance) {
      addSectoresToMap();
    }
  } catch (error) {
    console.error('Error al obtener sectores:', error);
  } finally {
    loading.value = false;
  }
};

const addSectoresToMap = () => {
  if (sectorLayers.length > 0) {
    sectorLayers.forEach(layer => map.removeLayer(layer));
    sectorLayers = [];
  }

  sectores.value.forEach((sector, index) => {
    try {
      if (!sector.area) {
        console.warn(`Sector ${sector.id} no tiene área definida`);
        return;
      }

      const geoJsonData = JSON.parse(sector.area);
      const color = sectorColors[index % sectorColors.length];

      const geoJsonLayer = L_instance.geoJSON(geoJsonData, {
        style: {
          color: color,
          fillColor: color,
          fillOpacity: 0.2,
          weight: 2
        }
      }).addTo(map);

      geoJsonLayer.bindPopup(`
        <div class="sector-popup">
          <h3 style="font-weight: bold; margin-bottom: 5px;">${sector.nombreSector}</h3>
        </div>
      `);
      sectorLayers.push(geoJsonLayer);
      if (index === 0 && geoJsonLayer.getBounds) {
        map.fitBounds(geoJsonLayer.getBounds(), {
          padding: [50, 50]
        });
      }
    } catch (e) {
      console.error(`Error parsing GeoJSON for sector ${sector.id}:`, e);
      const fallbackCircle = L_instance.circle(
          [props.initialLat, props.initialLng],
          {
            color: sectorColors[index % sectorColors.length],
            fillColor: sectorColors[index % sectorColors.length],
            fillOpacity: 0.2,
            weight: 2,
            radius: 500
          }
      ).addTo(map);

      fallbackCircle.bindPopup(`
        <div class="sector-popup">
          <h3 style="font-weight: bold; margin-bottom: 5px;">${sector.nombreSector}</h3>
        </div>
      `);

      sectorLayers.push(fallbackCircle);
    }
  });
};

onMounted(async () => {
  if (import.meta.client) {
    L_instance = (await import('leaflet')).default;
    await nextTick();

    if (mapContainer.value && !map) {
      map = L_instance.map(mapContainer.value).setView([props.initialLat, props.initialLng], props.initialZoom);

      L_instance.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19,
      }).addTo(map);

      if (props.selectedLatitude !== null && props.selectedLongitude !== null) {
        updateMarker(props.selectedLatitude, props.selectedLongitude);
      }

      map.on('click', (e: any) => {
        const { lat, lng } = e.latlng;
        console.log('[MapaComponent] Clic en mapa. Lat:', lat, 'Lng:', lng);
        updateMarker(lat, lng);
        emit('location-selected', { latitude: lat, longitude: lng });
      });

      setTimeout(() => {
        map?.invalidateSize();
      }, 150);
      await fetchSectores();
    }
  }
});

const updateMarker = (lat: number, lng: number) => {
  if (!map || !L_instance) return;
  const latLng = L_instance.latLng(lat, lng);
  if (selectionMarker) {
    selectionMarker.setLatLng(latLng);
  } else {
    selectionMarker = L_instance.marker(latLng).addTo(map);
  }
};

watch(() => [props.selectedLatitude, props.selectedLongitude], ([newLat, newLng]) => {
  if (import.meta.client && newLat !== null && newLng !== null && map && L_instance) {
    updateMarker(newLat, newLng);
  } else if (import.meta.client && newLat === null && newLng === null && selectionMarker && map) {
    map.removeLayer(selectionMarker);
    selectionMarker = null;
  }
});

onBeforeUnmount(() => {
  if (import.meta.client && map) {
    map.off('click');
    map.remove();
    map = null;
  }
});

defineExpose({
  refreshMapSize: () => {
    if (import.meta.client && map) {
      nextTick(() => {
        setTimeout(() => {
          map?.invalidateSize();
        }, 50);
      });
    }
  }
});
</script>

<style>
.leaflet-container {
  cursor: crosshair !important;
}

.sector-popup h3 {
  margin-top: 0;
  color: #333;
}

.sector-popup p {
  margin: 5px 0;
  font-size: 12px;
  color: #666;
}
</style>
