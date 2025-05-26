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
</style>