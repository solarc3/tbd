<template>
  <div class="chart-loading-container">
    <div class="chart-loading-spinner"></div>
    <p class="chart-loading-text">Cargando gráfico...</p>
  </div>
</template>

<script setup lang="ts">
// No se requiere ninguna lógica adicional
</script>

<style scoped>
.chart-loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  background-color: #f9f9f9;
  border-radius: 0.5rem;
}

.chart-loading-spinner {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-left-color: #3b82f6;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

.chart-loading-text {
  font-size: 0.875rem;
  color: #6b7280;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
