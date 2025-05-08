<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import EmblaCarousel from 'embla-carousel'
import Autoplay from 'embla-carousel-autoplay'

const emblaRef = ref<HTMLElement | null>(null)
let _emblaInstance: ReturnType<typeof EmblaCarousel> | null = null
const activeSlide = ref(0)

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

// Slide data with titles
const slides = [
  { image: '/slide4.jpeg', title: 'Bienvenidos a Nuestra Farmacia' },
  { image: '/slide5.png', title: 'Los Mejores Productos' },
  { image: '/slide3.jpg', title: 'Calidad Garantizada' },
]

// Carousel navigation functions
function prevSlide() {
  _emblaInstance?.scrollPrev()
}

function nextSlide() {
  _emblaInstance?.scrollNext()
}

function goToSlide(index: number) {
  _emblaInstance?.scrollTo(index)
}

// netamente es para redirigir filtrado a productos por categoria seleccionada en elhome
function goToCategory(category: Category) {
  router.push({ path: '/productos', query: { category: category.name } })
}

onMounted(() => {
  if (emblaRef.value) {
    _emblaInstance = EmblaCarousel(emblaRef.value, { loop: true }, [Autoplay({ delay: 3000 })])

    // Track current slide for dots navigation
    _emblaInstance.on('select', () => {
      activeSlide.value = _emblaInstance?.selectedScrollSnap() || 0
    })
  }
})
</script>

<template>
  <div class="px-4 sm:px-6 lg:px-8">
    <!-- carrusel mejorado -->
    <div class="embla-wrapper">
      <div class="embla rounded-lg shadow-xl overflow-hidden" ref="emblaRef">
        <div class="embla__container">
          <div v-for="(slide, index) in slides" :key="index" class="embla__slide">
            <img :src="slide.image" :alt="`Slide ${index + 1}`" class="slide-image" />
            <div class="slide-caption">
              <h2>{{ slide.title }}</h2>
            </div>
          </div>
        </div>

        <!-- Botones de navegación -->
        <button class="embla-button embla-button-prev" @click="prevSlide" aria-label="Anterior">
          &#10094;
        </button>
        <button class="embla-button embla-button-next" @click="nextSlide" aria-label="Siguiente">
          &#10095;
        </button>
      </div>

      <!-- Indicadores de posición -->
      <div class="embla-dots">
        <button
          v-for="(_, index) in slides"
          :key="index"
          :class="['embla-dot', { active: activeSlide === index }]"
          @click="goToSlide(index)"
        ></button>
      </div>
    </div>

    <!-- blocks de las imagenes para redireccion a productos -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4 mt-8 mb-8">
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
.embla-wrapper {
  max-width: 1850px; /* Mantiene tu ajuste */
  margin: 20px auto;
  position: relative;
}

.embla {
  overflow: hidden;
  width: 100%;
  position: relative;
}

.embla__container {
  display: flex;
}

.embla__slide {
  position: relative;
  flex: 0 0 100%;
}

.slide-image {
  width: 100%;
  height: 400px; /* Mantiene tu ajuste */
  object-fit: cover;
  transition: transform 0.3s ease;
}

.embla__slide:hover .slide-image {
  transform: scale(1.02);
}

.slide-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  padding: 20px;
  color: white;
  text-align: center;
}

.slide-caption h2 {
  font-size: 1.8rem;
  font-weight: bold;
  margin: 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

/* Botones de navegación */
.embla-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 50%;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 18px;
  z-index: 5;
  transition: all 0.2s ease;
}

.embla-button:hover {
  background: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.embla-button-prev {
  left: 20px;
}

.embla-button-next {
  right: 20px;
}

/* Puntos indicadores */
.embla-dots {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.embla-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #ccc;
  margin: 0 5px;
  padding: 0;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.embla-dot.active {
  background-color: #555;
  transform: scale(1.2);
}

img {
  width: 100%;
  height: 400px;
  object-fit: cover;
}
</style>
