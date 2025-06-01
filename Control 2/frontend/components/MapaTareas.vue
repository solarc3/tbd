<template>
  <Card class="h-full w-full !p-0">
    <CardContent class="p-0 h-full">
      <div
          ref="mapContainer"
          class="h-full w-full rounded-lg overflow-hidden"
      />
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch, computed } from "vue";
import { Card, CardContent } from "@/components/ui/card";
import "leaflet/dist/leaflet.css";
import { type SectorEntity } from "@/api/services/sectorService";

// Component props with defaults
const props = defineProps({
  initialLat: { type: Number, default: -33.444355 },
  initialLng: { type: Number, default: -70.653602 },
  initialZoom: { type: Number, default: 13 },
  selectedLatitude: { type: Number, default: null },
  selectedLongitude: { type: Number, default: null },
  sectores: { type: Array, default: () => [] },
  editable: { type: Boolean, default: true },
});

// Event emits
const emit = defineEmits(["location-selected"]);

// State variables
const mapContainer = ref(null);
const mapReady = ref(false);
let map = null;
let L = null;
let marker = null;
let sectorLayers = [];

// Colors for sectors
const sectorColors = [
  "#FF5733", "#33FF57", "#3357FF", "#F033FF", "#FF33A8",
  "#33FFF6", "#FFD133", "#8C33FF", "#FF8C33", "#33FFBD"
];

// Computed properties for coordinates
const coordinates = computed(() => {
  // Prioritize selected coordinates, fallback to initial coordinates
  return {
    lat: props.selectedLatitude !== null ? props.selectedLatitude : props.initialLat,
    lng: props.selectedLongitude !== null ? props.selectedLongitude : props.initialLng
  };
});

// Initialize the map
async function initializeMap() {
  if (!import.meta.client || !mapContainer.value) return;

  try {
    // Load Leaflet dynamically
    L = (await import("leaflet")).default;

    // Create the map instance
    map = L.map(mapContainer.value).setView(
        [coordinates.value.lat, coordinates.value.lng],
        props.initialZoom
    );

    // Add tile layer (map background)
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: "© OpenStreetMap contributors",
      maxZoom: 19,
    }).addTo(map);

    // Set up click handler if editable
    if (props.editable) {
      map.on("click", handleMapClick);
    }

    // Add marker if coordinates are provided
    updateMarker();

    // Handle any sectors if provided
    if (props.sectores?.length > 0) {
      addSectors();
    }

    // Make sure map renders correctly
    map.invalidateSize();

    mapReady.value = true;
  } catch (error) {
    console.error("Error initializing map:", error);
  }
}

// Update the marker position
function updateMarker() {
  if (!map || !L) return;

  // Get coordinates to use
  const lat = coordinates.value.lat;
  const lng = coordinates.value.lng;

  // If we already have a marker, update its position
  if (marker) {
    marker.setLatLng([lat, lng]);
  }
  // Otherwise create a new marker if we have valid coordinates or we're in non-editable mode
  else if ((props.selectedLatitude !== null && props.selectedLongitude !== null) || !props.editable) {
    marker = L.marker([lat, lng]).addTo(map);
  }
}

// Handle map clicks
function handleMapClick(e) {
  const { lat, lng } = e.latlng;

  // Update or create marker
  if (marker) {
    marker.setLatLng([lat, lng]);
  } else {
    marker = L.marker([lat, lng]).addTo(map);
  }

  // Emit event with selected coordinates
  emit("location-selected", { latitude: lat, longitude: lng });
}

// Add sectors to the map
function addSectors() {
  if (!map || !L || !props.sectores?.length) return;

  // Clear existing sector layers
  clearSectors();

  // Add each sector to the map
  props.sectores.forEach((sector, index) => {
    try {
      if (!sector.area) return;

      // Parse GeoJSON data
      const geoJsonData = JSON.parse(sector.area);
      const color = sectorColors[index % sectorColors.length];

      // Create GeoJSON layer
      const geoJsonLayer = L.geoJSON(geoJsonData, {
        style: {
          color: color,
          fillColor: color,
          fillOpacity: 0.2,
          weight: 2,
        },
      }).addTo(map);

      // Add popup with sector name
      geoJsonLayer.bindPopup(`
        <div class="sector-popup">
          <h3 style="font-weight: bold; margin-bottom: 5px;">${sector.nombreSector}</h3>
        </div>
      `);

      // Store layer reference
      sectorLayers.push(geoJsonLayer);

      // Fit map to first sector if available
      if (index === 0 && geoJsonLayer.getBounds) {
        map.fitBounds(geoJsonLayer.getBounds(), { padding: [50, 50] });
      }
    } catch (error) {
      console.warn(`Error adding sector ${sector.id}:`, error);

      // Fallback to a simple circle if GeoJSON parsing fails
      if (map && L) {
        const fallbackCircle = L.circle(
            [props.initialLat, props.initialLng],
            {
              color: sectorColors[index % sectorColors.length],
              fillColor: sectorColors[index % sectorColors.length],
              fillOpacity: 0.2,
              weight: 2,
              radius: 500,
            }
        ).addTo(map);

        fallbackCircle.bindPopup(`
          <div class="sector-popup">
            <h3 style="font-weight: bold; margin-bottom: 5px;">${sector.nombreSector}</h3>
          </div>
        `);

        sectorLayers.push(fallbackCircle);
      }
    }
  });
}

// Clear all sectors from the map
function clearSectors() {
  if (!map) return;

  sectorLayers.forEach(layer => map.removeLayer(layer));
  sectorLayers = [];
}

// Refresh map size (useful when container size changes)
function refreshMapSize() {
  if (map) {
    setTimeout(() => map.invalidateSize(), 50);
  }
}

// Component lifecycle hooks
onMounted(() => {
  if (import.meta.client) {
    initializeMap();
  }
});

onBeforeUnmount(() => {
  if (import.meta.client && map) {
    map.off("click", handleMapClick);
    map.remove();
    map = null;
  }
});

// Watch for changes in coordinates
watch([() => props.selectedLatitude, () => props.selectedLongitude], () => {
  if (map && L) {
    updateMarker();
  }
});

// Watch for changes in sectors
watch(() => props.sectores, (newSectores) => {
  if (map && L && newSectores?.length > 0) {
    addSectors();
  }
}, { deep: true });

// Expose methods for parent components
defineExpose({
  refreshMapSize,
  addSectors,
  clearSectors,
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
