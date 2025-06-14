<template>
  <Card class="h-full w-full !p-0">
    <CardContent class="p-0 h-full">
      <div ref="mapContainer" class="h-full w-full rounded-lg overflow-hidden" />
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import { Card, CardContent } from '@/components/ui/card'
import 'leaflet/dist/leaflet.css'

interface Props {
  initialLat?: number
  initialLng?: number
  initialZoom?: number
  selectedLatitude?: number | null
  selectedLongitude?: number | null
  editable?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  initialLat: -33.444355,
  initialLng: -70.653602,
  initialZoom: 13,
  selectedLatitude: null,
  selectedLongitude: null,
  editable: true
})

const emit = defineEmits<{
  'location-selected': [{ latitude: number, longitude: number }]
}>()

const mapContainer = ref<HTMLElement | null>(null)
let map: any
let L: any
let marker: any

function currentCoords() {
  return {
    lat: props.selectedLatitude ?? props.initialLat,
    lng: props.selectedLongitude ?? props.initialLng
  }
}

async function initializeMap() {
  if (!import.meta.client || !mapContainer.value) return

  const leaflet = await import('leaflet')
  L = leaflet.default

  const { lat, lng } = currentCoords()
  map = L.map(mapContainer.value).setView([lat, lng], props.initialZoom)

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 19
  }).addTo(map)

  if (props.editable) {
    map.on('click', handleMapClick)
  }

  updateMarker()
  map.invalidateSize()
}

function handleMapClick(e: any) {
  const { lat, lng } = e.latlng
  emit('location-selected', { latitude: lat, longitude: lng })

  if (marker) marker.setLatLng([lat, lng])
  else marker = L.marker([lat, lng]).addTo(map)
}

function updateMarker() {
  if (!map || !L) return
  const { lat, lng } = currentCoords()

  if (marker) {
    marker.setLatLng([lat, lng])
  } else if (lat != null && lng != null) {
    marker = L.marker([lat, lng]).addTo(map)
  }
}


function refreshMapSize() {
  if (map) setTimeout(() => map.invalidateSize(), 50)
}

onMounted(() => { initializeMap() })

onBeforeUnmount(() => {
  if (map) {
    map.off('click', handleMapClick)
    map.remove()
  }
})

watch(() => [props.selectedLatitude, props.selectedLongitude], updateMarker)

defineExpose({ refreshMapSize })
</script>

<style>
.leaflet-container {
  cursor: crosshair !important;
}

</style>
