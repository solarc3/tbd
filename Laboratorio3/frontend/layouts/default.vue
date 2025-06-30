<template>
  <div>
    <Header />
    <main>
      <slot />
      <Opiniones
          v-if="visible"
          :productos="productos"
          @close="popupStore.cerrar"
      />
    </main>
  </div>
</template>

<script setup lang="ts">
import Header from "~/components/layout/Header.vue";
import { ref, onMounted } from 'vue';
import Opiniones from '@/components/Opiniones.vue';
import { storeToRefs } from 'pinia'
import { useOpiniones } from '@/stores/opiniones'

const showOpiniones = ref(false)
const productosRecientes = ref<{ idProducto: number; nombre: string }[]>([])
const popupStore = useOpiniones()
const { visible, productos } = storeToRefs(popupStore)

onMounted(() => {
  if (!localStorage.getItem('opinionesPopupClosed')) {
    const data = localStorage.getItem('productosRecientes')
    if (data) {
      productosRecientes.value = JSON.parse(data)
      showOpiniones.value = true
    }
  }
})
</script>