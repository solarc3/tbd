<!-- https://echarts.apache.org/examples/en/index.html#chart-type-bar -->
<!-- https://echarts.apache.org/examples/en/editor.html?c=bar-simple&lang=ts -->

<template>
  <div class="p-6 bg-gray-50 min-h-screen">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">Reportes</h1>
    <!-- Botones -->
    <div class="flex justify-center space-x-4 mb-6">
      <button
          @click="activeTab = 'farmacias'"
          :class="activeTab === 'farmacias' ? 'border-blue-500 text-blue-500' : 'border-gray-300 text-gray-700'"
          class="w-full px-6 py-2 bg-white rounded-full border text-lg font-medium shadow-sm"
      >
        Farmacias
      </button>
      <button
          @click="activeTab = 'pedidos'"
          :class="activeTab === 'pedidos' ? 'border-blue-500 text-blue-500' : 'border-gray-300 text-gray-700'"
          class="w-full px-6 py-2 bg-white rounded-full border text-lg font-medium shadow-sm"
      >
        Pedidos
      </button>

      <button
          @click="activeTab = 'valoraciones'"
          :class="activeTab === 'valoraciones' ? 'border-blue-500 text-blue-500' : 'border-gray-300 text-gray-700'"
          class="w-full px-6 py-2 bg-white rounded-full border text-lg font-medium shadow-sm"
      >
        Valoraciones
      </button>
    </div>

    <!-- Farmacias -->
    <div v-if="activeTab === 'farmacias'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Farmacias con más entregas fallidas</h2>
        <v-chart :option="farmaciasFallidasOptions" style="height: 400px;" />
      </div>
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Farmacia con mayor producto de volúmenes entregados</h2>
        <v-chart :option="farmaciaMayorVolumenOptions" style="height: 400px;" />
      </div>
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Medio de pago utilizado en pedidos urgentes</h2>
        <v-chart :option="mediosPagoUrgentesOptions" style="height: 400px;" />
      </div>
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Top 3 repartidores con mejor rendimiento</h2>
        <v-chart :option="topRepartidoresOptions" style="height: 400px;" />
      </div>
    </div>

    <!-- query 1 - lab 3 -->
    <div v-if="activeTab === 'farmacias' || activeTab === 'valoraciones'" class="bg-white rounded-lg shadow-md p-6">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Promedio de puntuación por farmacia</h2>
        <div v-if="loadingPromedioFarmacias" class="flex justify-center items-center h-[200px]">
          <p>Cargando datos...</p>
        </div>
        <div v-else>
          <div v-if="promedioFarmacias.length === 0" class="text-center text-gray-500 py-10">
            No hay datos disponibles
          </div>
          <ul v-else class="divide-y divide-gray-200">
            <li v-for="farmacia in promedioFarmacias" :key="farmacia.nombreFarmacia" class="py-3 flex items-center justify-between">
              <span class="font-medium text-gray-800">{{ farmacia.nombreFarmacia }}</span>
              <span class="flex items-center gap-1">
                <span class="text-yellow-500 font-bold">{{ (farmacia.promedio ?? 0).toFixed(1) }}</span>
                <span class="flex">
                  <template v-for="i in 5" :key="i">
                    <!-- para que la estrella este llena (valores cerrados como 5.0 o 2.0 por ejemplop !) -->
                    <svg
                      v-if="i <= Math.floor(farmacia.promedio)"
                      class="w-4 h-4"
                      viewBox="0 0 20 20"
                    >
                      <path fill="#facc15" d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.967a1 1 0 00.95.69h4.175c.969 0 1.371 1.24.588 1.81l-3.38 2.455a1 1 0 00-.364 1.118l1.287 3.966c.3.922-.755 1.688-1.54 1.118l-3.38-2.454a1 1 0 00-1.175 0l-3.38 2.454c-.784.57-1.838-.196-1.54-1.118l1.287-3.966a1 1 0 00-.364-1.118L2.049 9.394c-.783-.57-.38-1.81.588-1.81h4.175a1 1 0 00.95-.69l1.286-3.967z"/>
                    </svg>
                    <!-- estrella a la mitad para valores como 2.5 !! -->
                    <svg
                      v-else-if="i === Math.floor(farmacia.promedio) + 1 && farmacia.promedio % 1 >= 0.5"
                      class="w-4 h-4"
                      viewBox="0 0 20 20"
                    >
                      <defs>
                        <linearGradient :id="`half-grad-${farmacia.nombreFarmacia.replace(/\s+/g, '_')}-${i}`" x1="0%" x2="100%" y1="0%" y2="0%">
                          <stop offset="50%" stop-color="#facc15"/>
                          <stop offset="50%" stop-color="#d1d5db"/>
                        </linearGradient>
                      </defs>
                      <path
                        :fill="`url(#half-grad-${farmacia.nombreFarmacia.replace(/\s+/g, '_')}-${i})`"
                        d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.967a1 1 0 00.95.69h4.175c.969 0 1.371 1.24.588 1.81l-3.38 2.455a1 1 0 00-.364 1.118l1.287 3.966c.3.922-.755 1.688-1.54 1.118l-3.38-2.454a1 1 0 00-1.175 0l-3.38 2.454c-.784.57-1.838-.196-1.54-1.118l1.287-3.966a1 1 0 00-.364-1.118L2.049 9.394c-.783-.57-.38-1.81.588-1.81h4.175a1 1 0 00.95-.69l1.286-3.967z"
                      />
                    </svg>
                    <!-- estrella vacia para literal las que no tienen nada lol -->
                    <svg
                      v-else
                      class="w-4 h-4"
                      viewBox="0 0 20 20"
                    >
                      <path fill="#d1d5db" d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.967a1 1 0 00.95.69h4.175c.969 0 1.371 1.24.588 1.81l-3.38 2.455a1 1 0 00-.364 1.118l1.287 3.966c.3.922-.755 1.688-1.54 1.118l-3.38-2.454a1 1 0 00-1.175 0l-3.38 2.454c-.784.57-1.838-.196-1.54-1.118l1.287-3.966a1 1 0 00-.364-1.118L2.049 9.394c-.783-.57-.38-1.81.588-1.81h4.175a1 1 0 00.95-.69l1.286-3.967z"/>
                    </svg>
                  </template>
                </span>
              </span>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Pedidos -->
    <div v-if="activeTab === 'pedidos'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Productos más pedidos por categoría</h2>
        <div v-if="loadingTopProducts" class="flex justify-center items-center h-[400px]">
          <p>Cargando datos...</p>
        </div>
        <div v-else class="overflow-y-auto" style="max-height: 400px;">
          <div v-if="categorizedProducts.length === 0" class="text-center text-gray-500 py-10">
            No hay datos disponibles
          </div>
          <div v-else>
            <div v-for="(category, index) in categorizedProducts" :key="index" class="mb-6">
              <h3 class="text-md font-medium text-blue-600 border-b pb-2 mb-3">{{ category.name }}</h3>
              <div class="space-y-2">
                <div v-for="product in category.products" :key="product.nombreProducto"
                     class="flex justify-between items-center py-2 px-3 hover:bg-gray-50 rounded">
                  <span class="text-gray-800">{{ product.nombreProducto }}</span>
                  <span class="bg-blue-100 text-blue-800 font-medium px-2.5 py-0.5 rounded-full">
                    {{ product.cantidadPedidos }} pedidos
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Pedidos que cruzan más de 2 zonas de reparto</h2>
        <div v-if="loadingPedidosCruzaZonas" class="flex justify-center items-center h-[400px]">
          <p>Cargando datos...</p>
        </div>
        <div v-else class="overflow-y-auto" style="max-height: 400px;">
          <div v-if="pedidosCruzaZonas.length === 0" class="text-center text-gray-500 py-10">
            No hay datos disponibles
          </div>
          <div v-else>
            <div v-for="pedido in pedidosCruzaZonas" :key="pedido.idPedido" class="mb-4">
              <button
                @click="pedido.expanded = !pedido.expanded"
                class="w-full text-left bg-gray-100 hover:bg-gray-200 px-4 py-2 rounded-md font-medium text-gray-800"
              >
                Pedido #{{ pedido.idPedido }} - {{ pedido.nombreCliente }}
              </button>
              <div v-if="pedido.expanded" class="mt-2 bg-gray-50 border rounded-md p-4">
                <p class="text-gray-800">Fecha: {{ pedido.fechaPedido }}</p>
                <p class="text-gray-800">Zonas cruzadas:</p>
                <ul class="list-disc pl-5">
                  <li v-for="zona in pedido.nombresZonas" :key="zona" class="text-gray-600">
                    {{ zona }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- query 3 - lab 3 -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">
          Pedidos con más de 3 cambios de estado en menos de 3 minutos
        </h2>
        <div v-if="loadingPedidosCambiosRapidos" class="flex justify-center items-center h-[200px]">
          <p>Cargando datos...</p>
        </div>
        <div v-else>
          <div v-if="pedidosCambiosRapidos.length === 0" class="text-center text-gray-500 py-10">
            No hay pedidos que cumplan este criterio.
          </div>
          <div v-else>
            <ul class="divide-y divide-gray-200">
              <li v-for="pedido in pedidosCambiosRapidos" :key="pedido.idPedido" class="py-3">
                <div class="flex flex-col md:flex-row md:items-center md:justify-between">
                  <div>
                    <span class="font-medium text-black-700">Pedido #{{ pedido.idPedido }}</span>
                  </div>
                  <div class="flex items-center gap-2">
                    <Badge :variant="getEstadoVariant(pedido.estadoPedido)">
                      {{ pedido.estadoPedido }}
                    </Badge>
                  </div>
                  <div class="text-gray-500 text-sm mt-1 md:mt-0">
                    Fecha: {{ pedido.fechaPedido.replace('T', ' ').slice(0, 16) }}
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <!-- valoraciones -->
    <div v-if="activeTab === 'valoraciones'" class="bg-white rounded-lg shadow-md p-6">
      <h2 class="text-lg font-semibold text-gray-700 mb-4">Valoraciones por hora del día</h2>
      <div class="mb-4 flex items-center gap-4">
        <label class="font-medium text-gray-700">Selecciona un día:</label>
        <input type="date" v-model="selectedDate" class="border rounded px-2 py-1" />
      </div>
      <div v-if="loadingOpinionesPorHora" class="flex justify-center items-center h-[200px]">
        <p>Cargando datos...</p>
      </div>
      <div v-else>
        <v-chart :option="opinionesPorHoraOptions" style="height: 400px;" />
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, reactive, watch } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, PieChart, LineChart } from 'echarts/charts'
import { LegendComponent, GridComponent, TooltipComponent, TitleComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { Badge } from "~/components/ui/badge";

// importar apis
import { farmaciaService, repartidorService, pedidoService, productoService } from '@/api/services'
import type { PedidoCruzaZonas } from '@/api/models';

use([CanvasRenderer, BarChart, PieChart, LineChart, GridComponent, TooltipComponent, TitleComponent, LegendComponent])

// verificacion cliente nomas
if (typeof window !== 'undefined') {
  const { use } = await import('echarts/core')
  const { CanvasRenderer } = await import('echarts/renderers')
  const { BarChart, PieChart } = await import('echarts/charts')
  const { LegendComponent, GridComponent, TooltipComponent, TitleComponent } = await import('echarts/components')
  const VChart = (await import('vue-echarts')).default

  use([CanvasRenderer, BarChart, PieChart, GridComponent, TooltipComponent, TitleComponent, LegendComponent])
}

// estados para aplicar "reactive"
const loadingFarmacias = ref(true)
const loadingRepartidores = ref(true)
const loadingPedidos = ref(true)
const activeTab = ref('farmacias')
const topProductsData = ref<any[]>([])

// Datos categorizados
const categorizedProducts = computed(() => {
  const groupedByCategory = topProductsData.value.reduce((acc, product) => {
    const category = product.categoria
    if (!acc[category]) acc[category] = []
    acc[category].push(product)
    return acc
  }, {})
  return Object.keys(groupedByCategory).map(category => ({
    name: category,
    products: groupedByCategory[category]
  }))
})

// script updateado para "farmacias con mas entregas fallidas"
const farmaciasFallidasOptions = reactive({
  title: {
    text: 'Farmacias con más entregas fallidas',
    left: 'center',
  },
  xAxis: {
    type: 'category',
    data: [] as string[],
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      name: 'Entregas fallidas',
      type: 'bar',
      data: [] as number[],
      itemStyle: {
        color: '#df25ab',
      },
    },
  ],
})

// OJO para este script tenemos super poquitos medios de pago
// entonces se ve una pura linea del gráfico, considerar poner mas en data.sql
// script updateado "medio de pago utilizado en pedidos urgentes"
const mediosPagoUrgentesOptions = reactive({
  title: {
    text: 'Medio de pago utilizado en pedidos urgentes',
    left: 'center',
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    },
    formatter: function (params) {
      return `${params[0].name}<br/>Pedidos: ${params[0].value}`;
    }
  },
  xAxis: {
    type: 'category',
    data: [] as string[],
    axisLabel: {
      interval: 0,
      rotate: 30
    }
  },
  yAxis: {
    type: 'value',
    name: 'Cantidad de Pedidos'
  },
  series: [
    {
      name: 'Pedidos urgentes',
      type: 'bar',
      data: [] as number[],
      itemStyle: {
        color: '#f87e02',
      },
    },
  ],
})

// script updateado "top 3 repartidores con mejor rendimiento"
const topRepartidoresOptions = reactive({
  title: {
    text: 'Top 3 repartidores con mejor rendimiento',
    left: 'center',
  },
  tooltip: {
    trigger: 'axis',
    formatter: function (params) {
      // formateo extra para cuando se haga hover en las barras
      // muestra nombre, indice, pedidos y puntuacion
      const data = params[0].data;
      return `${params[0].name}<br/>
              Índice: ${data.indice}<br/>
              Pedidos: ${data.pedidos}<br/>
              Puntuación: ${data.puntuacion.toFixed(2)}`;
    }
  },
  xAxis: {
    type: 'value',
    name: 'Índice de Rendimiento'
  },
  yAxis: {
    type: 'category',
    data: [] as string[],
    axisLabel: {
      interval: 0,
      rotate: 0
    }
  },
  series: [
    {
      name: 'Rendimiento',
      type: 'bar',
      data: [] as any[],
      itemStyle: {
        color: '#3669eb',
      },
    },
  ],
})

const loadingFarmaciaRanking = ref(true);
const farmaciaMayorVolumenOptions = reactive({
  title: {
    text: 'Farmacia con mayor producto de volúmenes entregados',
    left: 'center',
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    },
    formatter: function (params) {
      return `${params[0].name}<br/>Pedidos entregados: ${params[0].value}`;
    }
  },
  xAxis: {
    type: 'category',
    data: [] as string[],
    axisLabel: {
      interval: 0,
      rotate: 30
    }
  },
  yAxis: {
    type: 'value',
    name: 'Cantidad de pedidos'
  },
  series: [
    {
      name: 'Pedidos entregados',
      type: 'bar',
      data: [] as number[],
      itemStyle: {
        color: '#8e56ff',
      },
    },
  ],
});

const loadingTopProducts = ref(true)

const topProductsByCategoryOptions = reactive({
  title: {
    text: 'Productos más pedidos por categoría',
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} pedidos'
  },
  legend: {
    type: 'scroll',
    orient: 'vertical',
    right: 10,
    top: 50,
    bottom: 20,
  },
  series: [
    {
      name: 'Categoría',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [] as { value: number; name: string; categoryName: string }[]
    }
  ]
})

const loadingPedidosCruzaZonas = ref(true);
const pedidosCruzaZonas = ref<PedidoCruzaZonas[]>([]);

// query 1 - lab 3 !!
const loadingPromedioFarmacias = ref(true);
const promedioFarmacias = ref<{ nombreFarmacia: string; promedio: number }[]>([]);

// query 3 - lab 3 !!
const loadingPedidosCambiosRapidos = ref(true);
const pedidosCambiosRapidos = ref<any[]>([]);

const getEstadoVariant = (estado: string) => {
  switch (estado) {
    case "ENTREGADO":
      return "success";
    case "CONFIRMADO":
      return "info";
    case "POR_CONFIRMAR":
      return "warning";
    case "CANCELADO":
      return "destructive";
    default:
      return "default";
  }
};

// query 6 - lab 3 !!
const loadingOpinionesPorHora = ref(true);
const opinionesPorHoraRaw = ref<{ [hora: string]: any[] }>({});
const selectedDate = ref(new Date().toISOString().slice(0, 10));

// filtrar opiniones por fecha seleccionada y agrupar por hora
const opinionesPorHoraOptions: Ref<EChartsOption> = ref({
  title: { text: 'Satisfacción promedio por hora', left: 'center' },
  tooltip: {
    trigger: 'axis',
    formatter: (params: any) => {
      const [line, bar] = params
      return `
        Hora: ${line.axisValue}<br>
        Promedio de puntuación: <b>${line.data?.toFixed(2) ?? '-'}</b><br>
        Cantidad de opiniones: <b>${bar.data ?? 0}</b>
      `
    }
  },
  xAxis: {
    type: 'category',
    data: Array.from({ length: 24 }, (_, i) => `${i}:00`),
    name: 'Hora del día'
  },
  yAxis: [
    { type: 'value', name: 'Promedio', min: 0, max: 5 },
    { type: 'value', name: 'Cantidad', min: 0 }
  ],
  series: []
})

// promedio de puntuacion y cantidad de opiniones por hora
const opinionesPorHoraStats = computed(() => {
  const promedios: number[] = Array(24).fill(0)
  const cantidades: number[] = Array(24).fill(0)
  Object.entries(opinionesPorHoraRaw.value).forEach(([hora, opiniones]) => {
    const filtradas = opiniones.filter(op => op.fecha && op.fecha.startsWith(selectedDate.value))
    cantidades[parseInt(hora, 10)] = filtradas.length
    if (filtradas.length > 0) {
      const suma = filtradas.reduce((acc, op) => acc + (op.puntuacion ?? 0), 0)
      promedios[parseInt(hora, 10)] = suma / filtradas.length
    }
  })
  return { promedios, cantidades }
})

// se actualiza el grafo
watch([opinionesPorHoraStats, selectedDate], () => {
  opinionesPorHoraOptions.value.series = [
    {
      name: 'Promedio de puntuación',
      type: 'line',
      data: opinionesPorHoraStats.value.promedios,
      smooth: true,
      itemStyle: { color: '#fb00e6' },
      yAxisIndex: 0,
      symbolSize: 12,
      label: { show: false }
    },
    {
      name: 'Cantidad de opiniones',
      type: 'bar',
      data: opinionesPorHoraStats.value.cantidades,
      yAxisIndex: 1,
      itemStyle: { color: '#ffffff', opacity: 0.3 },
      barWidth: 10,
      label: {
        show: false
      }
    }
  ]
  opinionesPorHoraOptions.value.yAxis = [
    { type: 'value', name: 'Promedio', min: 0, max: 5 },
    { type: 'value', name: 'Cantidad', min: 0 }
  ]
}, { immediate: true })

onMounted(async () => {
  try {
    const pedidos = await pedidoService.getPedidosCruzaZonas(3)
    pedidosCruzaZonas.value = pedidos
    loadingPedidosCruzaZonas.value = false

    const topProducts = await productoService.getTopProductsByCategory()
    topProductsData.value = topProducts
    loadingTopProducts.value = false
  } catch (error) {
    console.error(error)
  }

  try {
    // consumir api de farmacias y obtener las farmacias con pedidos fallidos
    console.log('Fetching farmacias falladas from API...')
    const farmaciasFalladas = await farmaciaService.getFarmaciasFalladas()
    console.log('Farmacias falladas data:', farmaciasFalladas)

    // DEBUGING ME VOY A VOLVER CHANGO
    // ahi si
    if (farmaciasFalladas && Array.isArray(farmaciasFalladas)) {
      // updatear grafico
      // se mapea el array para obtener los nombres y cantidades
      // si no se encuentra el nombre, se pone "Sin nombre"
      farmaciasFallidasOptions.xAxis.data = farmaciasFalladas.map(f => f.nombreFarmacia || 'Sin nombre')
      farmaciasFallidasOptions.series[0].data = farmaciasFalladas.map(f =>
          typeof f.cantidad === 'number' ? f.cantidad : 0
      )
    }

    loadingFarmacias.value = false
  } catch (error) {
    console.error('Error fetching farmacias falladas:', error)
    // datos x defecto para no romper el grafico en caso de error
    farmaciasFallidasOptions.xAxis.data = ['Error']
    farmaciasFallidasOptions.series[0].data = [0]
    loadingFarmacias.value = false
  }

  // Farmacia Ranking
  try {
    console.log('Fetching farmacia ranking data...')
    loadingFarmaciaRanking.value = true
    const farmaciaRanking = await farmaciaService.getFarmaciasRanking()
    console.log('Farmacia ranking data:', farmaciaRanking)

    if (farmaciaRanking && Array.isArray(farmaciaRanking) && farmaciaRanking.length > 0) {
      console.log('First item sample:', farmaciaRanking[0])

      const sortedData = [...farmaciaRanking].sort((a, b) =>
          b.cantPedidosEntregados - a.cantPedidosEntregados
      )

      farmaciaMayorVolumenOptions.xAxis.data = sortedData.map(f => f.nombreFarmacia || 'Sin nombre')
      farmaciaMayorVolumenOptions.series[0].data = sortedData.map(f =>
          typeof f.cantPedidosEntregados === 'number' ? f.cantPedidosEntregados : 0
      )
    } else {
      console.warn('No farmacia ranking data found or invalid format', farmaciaRanking)
      farmaciaMayorVolumenOptions.xAxis.data = ['Sin datos disponibles']
      farmaciaMayorVolumenOptions.series[0].data = [0]
    }
    loadingFarmaciaRanking.value = false
  } catch (error) {
    console.error('Error fetching farmacia ranking:', error)
    // Default data to avoid breaking the chart
    farmaciaMayorVolumenOptions.xAxis.data = ['Error al cargar datos']
    farmaciaMayorVolumenOptions.series[0].data = [0]
    loadingFarmaciaRanking.value = false
  }

  // para el grafico de "top 3 repartidores con mejor rendimiento"
  try {
    // sacar top 3 repartidores con mejor rendimiento
    // console log para debugear, se puede sacar dsps
    console.log('Fetching top repartidores...')
    const repartidores = await repartidorService.getTopRepartidores()
    console.log('Repartidores data:', repartidores)

    // para arreglar skill issue de que no se haga top 3
    // sortear por indice de rendimiento y sacar los 3 primeros
    const top3Repartidores = [...repartidores]
        .sort((a, b) => b.indiceRendimiento - a.indiceRendimiento)
        .slice(0, 3)

    // se tiene que dar vuelta el array para que se vea de arriba a abajo
    // pq echarts lo hace al revez plop
    const reversedRepartidores = [...top3Repartidores].reverse()

    topRepartidoresOptions.yAxis.data = reversedRepartidores.map(r => r.nombreRepartidor)

    // guardar datos en "tooltip" para que se vean al hacer hover
    topRepartidoresOptions.series[0].data = reversedRepartidores.map(r => ({
      value: r.indiceRendimiento,
      indice: r.indiceRendimiento.toFixed(1),
      pedidos: r.pedidos,
      puntuacion: r.puntuacionPromedio
    }))
    loadingRepartidores.value = false

  } catch (error) {
    console.error('Error fetching top repartidores:', error)
    // se ponen datos default para no romper el grafico en caso de error
    topRepartidoresOptions.yAxis.data = ['Error al cargar datos']
    topRepartidoresOptions.series[0].data = [0]
    loadingRepartidores.value = false
  }

  // para el gráfico de "medio de pago utilizado en pedidos urgentes"
  try {
    // sacar los medios de pago utilizados en pedidos urgentes
    const mediosPago = await pedidoService.getMediosPagoUrgentes()

    if (mediosPago && Array.isArray(mediosPago)) {
      // se actualiza el grafico con los datos obtenidos del api request
      mediosPagoUrgentesOptions.xAxis.data = mediosPago.map(m => m.metodoPago || 'No especificado')
      // se aplica un map para obtener la cantidad de cada metodo de pago
      // si no se encuentra la cantidad, se pone 0
      mediosPagoUrgentesOptions.series[0].data = mediosPago.map(m =>
          typeof m.cantidad === 'number' ? m.cantidad : 0
      )

    } else {
      console.error('Medios de pago: Data is not an array', mediosPago)
    }

    loadingPedidos.value = false
  } catch (error) {
    // tira error y pone datos default para que no se vea feo
    console.error('Error fetching medios de pago urgentes:', error)
    mediosPagoUrgentesOptions.xAxis.data = ['Error al cargar datos']
    mediosPagoUrgentesOptions.series[0].data = [0]
    loadingPedidos.value = false
  }

  try {
    console.log('Fetching top products by category...')
    loadingTopProducts.value = true

    const topProducts = await productoService.getTopProductsByCategory()
    console.log('Top products data:', topProducts)

    if (topProducts && Array.isArray(topProducts) && topProducts.length > 0) {
      // Store the raw data instead of formatting for pie chart
      topProductsData.value = topProducts
    } else {
      console.warn('No top products data found or invalid format', topProducts)
      topProductsData.value = []
    }

    loadingTopProducts.value = false
  } catch (error) {
    console.error('Error fetching top products by category:', error)
    topProductsData.value = []
    loadingTopProducts.value = false
  }

  // lab 2 - pedido + 2 zonas
  try {
    console.log('Fetching pedidos que cruzan más de 2 zonas...');
    const pedidos = await pedidoService.getPedidosCruzaZonas(3);
    pedidosCruzaZonas.value = pedidos.map(pedido => ({ ...pedido, expanded: false }));
    console.log('Pedidos que cruzan más de 2 zonas:', pedidos);
  } catch (error) {
    console.error('Error fetching pedidos que cruzan más de 2 zonas:', error);
    pedidosCruzaZonas.value = [];
  } finally {
    loadingPedidosCruzaZonas.value = false;
  }

  // query 3 - pedidos con mas de 3 cambios de estado en menos de 3 minutos
  try {
    loadingPedidosCambiosRapidos.value = true;
    const data = await pedidoService.getPedidosCambiosRapidos();
    pedidosCambiosRapidos.value = data && data.pedidos ? data.pedidos : [];
  } catch (error) {
    console.error('Error fetching pedidos cambios rápidos:', error);
    pedidosCambiosRapidos.value = [];
  } finally {
    loadingPedidosCambiosRapidos.value = false;
  }

  // query 1 - promedio de puntuacion por farmacia
  try {
    loadingPromedioFarmacias.value = true
    const data = await farmaciaService.getPromedioPuntuacionPorFarmacia()
    promedioFarmacias.value = data.map(f => ({
      nombreFarmacia: f.nombreFarmacia,
      promedio: f.promedioPuntuacion ?? 0
    }))
  } catch (error) {
    promedioFarmacias.value = []
  } finally {
    loadingPromedioFarmacias.value = false
  }

  try {
    loadingOpinionesPorHora.value = true
    opinionesPorHoraRaw.value = await farmaciaService.getOpinionesPorHora()
  } catch (error) {
    opinionesPorHoraRaw.value = {}
  } finally {
    loadingOpinionesPorHora.value = false
  }
})

</script>

<style scoped>
.grid {
  gap: 1.5rem;
}
</style>