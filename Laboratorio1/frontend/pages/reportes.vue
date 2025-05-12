<!-- https://echarts.apache.org/examples/en/index.html#chart-type-bar -->
<!-- https://echarts.apache.org/examples/en/editor.html?c=bar-simple&lang=ts -->

<template>
  <div class="p-6 bg-gray-50 min-h-screen">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">Reportes</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-6">
      <!-- farmacias con mas entregas fallidas -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-lg font-semibold text-gray-700 mb-4">Farmacias con más entregas fallidas</h2>
        <div v-if="loadingFarmacias" class="flex justify-center items-center h-[400px]">
          <p>Cargando datos...</p>
        </div>
        <v-chart v-else :option="farmaciasFallidasOptions" style="height: 400px;" />
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
      <!-- Replace the existing Top products by category div with this: -->
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, reactive } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, PieChart } from 'echarts/charts'
import { LegendComponent, GridComponent, TooltipComponent, TitleComponent } from 'echarts/components'
import VChart from 'vue-echarts'

// importar apis
import { farmaciaService, repartidorService, pedidoService, productoService } from '@/api/services'

use([CanvasRenderer, BarChart, PieChart, GridComponent, TooltipComponent, TitleComponent, LegendComponent])

// estados para aplicar "reactive"
const loadingFarmacias = ref(true)
const loadingRepartidores = ref(true)
const loadingPedidos = ref(true)
const topProductsData = ref<any[]>([])

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
        color: '#fb7e00',
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

// Replace the farmaciaMayorVolumenOptions declaration with this:

// Make chart options reactive
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
      rotate: 30  // Rotate labels for better visibility
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

// Add loading state for top products
const loadingTopProducts = ref(true)

// Configuration for top products by category chart
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

const categorizedProducts = computed(() => {
    // Group by category
    const groupedByCategory = topProductsData.value.reduce((acc, product) => {
      const category = product.categoria
      if (!acc[category]) {
        acc[category] = []
      }
      acc[category].push(product)
      return acc
    }, {})
    
    // Convert to array format for v-for
    return Object.keys(groupedByCategory).map(category => ({
      name: category,
      products: groupedByCategory[category]
    }))
  })

onMounted(async () => {
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

  //Farmacia Ranking
  // Replace the Farmacia Ranking section in onMounted with this:

// Farmacia Ranking
try {
  console.log('Fetching farmacia ranking data...')
  loadingFarmaciaRanking.value = true
  const farmaciaRanking = await farmaciaService.getFarmaciasRanking()
  console.log('Farmacia ranking data:', farmaciaRanking)
  
  if (farmaciaRanking && Array.isArray(farmaciaRanking) && farmaciaRanking.length > 0) {
    // Make sure we're getting the data in the right format
    console.log('First item sample:', farmaciaRanking[0])
    
    // Sort by cantPedidosEntregados descending for better visualization
    const sortedData = [...farmaciaRanking].sort((a, b) => 
      b.cantPedidosEntregados - a.cantPedidosEntregados
    )
    
    // Update chart with the data from API
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
})

</script>

<style scoped>
.grid {
  gap: 1.5rem;
}
</style>