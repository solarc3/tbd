<script setup lang="ts">
import { ref, computed } from 'vue'
import { ShoppingCart } from 'lucide-vue-next' // ya use esto en varias partes pero se m olvido ponerlo xd, usenlo para
                                               // ver los distintos iconos que les podria gustar cambiar, aca use el
                                               // del carrito x ejemplo !!

// productos q tenemos que setear dsps por categoria para que funcione el filtro y redireccionamiento del inicio
const products = ref([
  { id: 1, name: 'Limpiador Control Imperfecciones CeraVe 236ml', description: 'descripcion aa', price: 13599, image: '/crema.png', category: 'dermatologia' },
  { id: 2, name: 'Pasta Dental Colgate Total Clean Mint', description: 'descripcion aa', price: 6499, image: '/pastadental.png', category: 'higiene' },
  { id: 3, name: 'Paracetamol 16 compromidos', description: 'descripcion aa', price: 1500, image: '/paracetamol.png', category: 'medicamentos' },
  { id: 4, name: 'VitaminLife Proteína Whey Cookies & Crema 907g', description: 'descripcion aa', price: 47399, image: '/suplemento.png', category: 'suplementos' },
  { id: 5, name: 'Bálsamo Labial Nivea Cherry Shine', description: 'descripcion aa', price: 4990, image: '/labialnivea.png', category: 'cosmeticos' },
])

// categorias q puse, igual pueden ser mas pero por las farmacias q estuve viendo son las + importantes
const categories = ref(['dermatologia', 'higiene', 'medicamentos', 'suplementos', 'cosmeticos'])
const selectedCategory = ref('')
const filteredProducts = computed(() => {
  if (!selectedCategory.value) return products.value
  return products.value.filter(product => product.category === selectedCategory.value)
})

const route = useRoute()
onMounted(() => {
  const categoryFromQuery = route.query.category
  if (categoryFromQuery && categories.value.includes(categoryFromQuery as string)) {
    selectedCategory.value = categoryFromQuery as string
  }
})

</script>

<template>
  <div class="container mx-auto px-4 py-8 flex">
    <!-- para gestionar el "menu" del costado veanlo aca !! -->
    <aside class="w-64 mr-6">
      <h2 class="text-lg font-bold text-gray-800 mb-4">Categorías</h2>
      <ul class="space-y-2">
        <li
          v-for="category in categories"
          :key="category"
          class="cursor-pointer text-gray-700 hover:text-blue-500"
          :class="{ 'font-bold text-blue-500': selectedCategory === category }"
          @click="selectedCategory = category"
        >
          {{ category.charAt(0).toUpperCase() + category.slice(1) }}
        </li>
        <li
          class="cursor-pointer text-gray-700 hover:text-blue-500"
          :class="{ 'font-bold text-blue-500': selectedCategory === '' }"
          @click="selectedCategory = ''"
        >
          Todos
        </li>
      </ul>
    </aside>

    <div class="flex-1">
      <h1 class="text-2xl font-bold text-gray-800 mb-6 text-center">Productos disponibles</h1>
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <div
          v-for="product in filteredProducts"
          :key="product.id"
          class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
        >
          <!-- esta linea es dms importante pq hace q se vea la imagen cmo perfecta en las cajas, por si no les da bn la dimension -->
          <img :src="product.image" :alt="product.name" class="w-full h-48 object-contain bg-white-100" />
          <div class="p-4">
            <h2 class="text-lg font-semibold text-gray-800">{{ product.name }}</h2>
            <p class="text-sm text-gray-600">{{ product.description }}</p>
            <p class="text-lg font-bold text-blue-500 mt-2">${{ product.price.toLocaleString() }}</p>
            <button
                class="btn-custom mt-4 w-full flex items-center justify-center space-x-2"
            >
              <ShoppingCart class="w-5 h-5" />
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  background-color: #f9fafb;
  min-height: 100vh;
}

</style>