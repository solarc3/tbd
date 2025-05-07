<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import EmblaCarousel from 'embla-carousel'
import Autoplay from 'embla-carousel-autoplay'

const emblaRef = ref<HTMLElement | null>(null)
let _emblaInstance: ReturnType<typeof EmblaCarousel> | null = null

const router = useRouter()

// categorias nms
const categories = [
  { name: 'dermatologia', image: '/dermatologia.png' },
  { name: 'higiene', image: '/higiene.png' },
  { name: 'medicamentos', image: '/medicamentos.png' },
  { name: 'suplementos', image: '/suplementos.png' },
  { name: 'cosmeticos', image: '/cosmeticos.png' },
]

type Category = {
  name: string
  image: string
}

// netamente es para redirigir filtrado a productos por categoria seleccionada en elhome
function goToCategory(category: Category) {
  router.push({ path: '/productos', query: { category: category.name } })
}

onMounted(() => {
  if (emblaRef.value) {
    _emblaInstance = EmblaCarousel(emblaRef.value, { loop: true }, [Autoplay({ delay: 2000 })])
  }
})
</script>

<template>
  <div>
    <!-- carrusel hay que ver bn las dimensiones y q poner ToT -->
    <!-- aca puse mejor la dimension horizontal en 1850px para que coincida con los blocks -->
    <div class="embla" ref="emblaRef">
      <div class="embla__container">
        <div class="embla__slide">
          <img src="/slide4.jpeg" alt="Slide 1" />
        </div>
        <div class="embla__slide">
          <img src="/slide5.png" alt="Slide 2" />
        </div>
        <div class="embla__slide">
          <img src="/slide3.jpg" alt="Slide 3" />
        </div>
      </div>
    </div>

    <!-- blocks de las imagenes para redireccion a productos -->
    <!-- le acabo de poner un margen chiquito con el borde de la pagina (px-4) -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4 mt-8 px-4">
      <div
        v-for="category in categories"
        :key="category.name"
        class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
      >
        <img
          :src="category.image"
          :alt="category.name"
          class="w-full h-48 object-cover cursor-pointer"
          @click="goToCategory(category)"
        />
        <div class="p-4 text-center">
          <button
            class="btn-custom w-full"
            @click="goToCategory(category)"
          >
            {{ category.name.charAt(0).toUpperCase() + category.name.slice(1) }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.embla {
  overflow: hidden;
  width: 100%;
  max-width: 1850px; /* aca!! */
  margin: 20px auto; /* margen con la navbar, lo cambie para que no hubiera tanto desplazamiento vertical */
}

.embla__container {
  display: flex;
}

.embla__slide {
  position: relative;
  flex: 0 0 100%;
}

img {
  width: 100%;
  height: 400px; /* si lo ponemos en 400 se ve bien igual pero queda todo muy grande */
  object-fit: cover;
}
</style>