<!-- https://echarts.apache.org/examples/en/index.html#chart-type-bar -->
<!-- https://echarts.apache.org/examples/en/editor.html?c=bar-simple&lang=ts -->

<template>
  <div class="p-6 bg-gray-50 min-h-screen">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">Reportes</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6">
      <!-- farmacias con mas entregas fallidas -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Farmacias con más entregas fallidas</h2>
        <v-chart :option="farmaciasFallidasOptions" style="height: 400px;" />
      </div>

      <!-- top 3 repartidores con mejor rendimiento -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Top 3 repartidores con mejor rendimiento</h2>
        <v-chart :option="topRepartidoresOptions" style="height: 400px;" />
      </div>

      <!-- medio de pago utilizado en pedidos urgentes -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Medio de pago utilizado en pedidos urgentes</h2>
        <v-chart :option="mediosPagoUrgentesOptions" style="height: 400px;" />
      </div>

      <!-- farmacia con mayor producto de volumenes entregados -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Farmacia con mayor producto de volúmenes entregados</h2>
        <v-chart :option="farmaciaMayorVolumenOptions" style="height: 400px;" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, TitleComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, TitleComponent])

// script chiquito pal grafo d prueba d "farmacias con mas entregas fallidas"
const farmaciasFallidasOptions = {
  title: {
    text: 'Farmacias con más entregas fallidas',
    left: 'center',
  },
  xAxis: {
    type: 'category',
    data: ['Farmacia A', 'Farmacia B', 'Farmacia C'],
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      name: 'Entregas fallidas',
      type: 'bar',
      data: [15, 10, 8],
      itemStyle: {
        color: '#df25ab',
      },
    },
  ],
}

// script chiquito pal grafo d prueba d "medio de pago utilizado en pedidos urgentes"
const mediosPagoUrgentesOptions = {
  title: {
    text: 'Medio de pago utilizado en pedidos urgentes',
    left: 'center',
  },
  xAxis: {
    type: 'category',
    data: ['Tarjeta', 'Efectivo', 'Transferencia'],
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      name: 'Pedidos urgentes',
      type: 'bar',
      data: [50, 30, 20],
      itemStyle: {
        color: '#fb7e00',
      },
    },
  ],
}

// script chiquito pal grafo d prueba d  "top 3 repartidores con mejor rendimiento"
const topRepartidoresOptions = {
  title: {
    text: 'Top 3 repartidores con mejor rendimiento',
    left: 'center',
  },
  xAxis: {
    type: 'value',
  },
  yAxis: {
    type: 'category',
    data: ['Repartidor 1', 'Repartidor 2', 'Repartidor 3'],
  },
  series: [
    {
      name: 'Rendimiento (%)',
      type: 'bar',
      data: [100, 90, 80],
      itemStyle: {
        color: '#3669eb',
      },
    },
  ],
}

// script chiquito pal grafo d prueba d "farmacia con mayor producto de volumenes entregados"
const farmaciaMayorVolumenOptions = {
  title: {
    text: 'Farmacia con mayor producto de volúmenes entregados',
    left: 'center',
  },
  xAxis: {
    type: 'category',
    data: ['Farmacia X'],
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      name: 'Volumen entregado',
      type: 'bar',
      data: [100],
      itemStyle: {
        color: '#8e56ff',
      },
    },
  ],
}

</script>

<style scoped>
.grid {
  gap: 1.5rem;
}
</style>