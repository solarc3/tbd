<script setup lang="ts">
import { computed } from 'vue';
import { AlertTriangle } from 'lucide-vue-next';

const props = defineProps({
  cartItems: {
    type: Array,
    required: true
  },
  total: {
    type: Number,
    required: true
  },
  prescriptionConfirmations: {
    type: Object,
    required: true
  }
});

const hasRxItems = computed(() => {
  return props.cartItems.some(item => item.requiereReceta);
});
</script>
<template>
    <div class="mt-8">
      <h3 class="text-lg font-semibold text-gray-700 mb-4">Confirmar detalles</h3>
      <div>
        <!-- Order items summary -->
        <div class="bg-white rounded-lg shadow-sm p-6 border mb-6">
          <h4 class="font-medium text-gray-800 mb-4 pb-2 border-b">Resumen de productos</h4>
          
          <div class="space-y-4">
            <div v-for="item in cartItems" :key="item.idProducto" class="flex py-3 border-b border-gray-100 last:border-0">
              <!-- Item image (smaller than in cart) -->
              <div class="w-16 h-16 bg-gray-100 rounded-md overflow-hidden flex-shrink-0">
                <img v-if="item.image" :src="item.image" :alt="item.nombreProducto" class="w-full h-full object-cover" />
                <div v-else class="w-full h-full flex items-center justify-center bg-gray-200">
                  <span class="text-gray-400 text-xs">Sin imagen</span>
                </div>
              </div>
              
              <!-- Item details -->
              <div class="ml-3 flex-grow">
                <div class="flex justify-between">
                  <h5 class="font-medium text-gray-800">{{ item.nombreProducto }}</h5>
                  <p class="font-medium text-gray-800">${{ (item.precio * item.cantidad).toLocaleString() }}</p>
                </div>
                <div class="flex justify-between text-sm text-gray-500 mt-1">
                  <span>{{ item.cantidad }} x ${{ item.precio.toLocaleString() }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="space-y-2 mb-4">
            <div class="flex justify-between py-2 mt-2 border-t font-bold text-lg">
              <span>Total</span>
              <span>${{ total.toLocaleString() }}</span>
            </div>
          </div>
        </div>
        <div v-if="hasRxItems" class="mt-6 pt-4 border-t">
          <div class="flex items-start space-x-3 bg-amber-50 p-4 rounded-md border border-amber-200">
            <AlertTriangle class="w-6 h-6 text-amber-500 flex-shrink-0 mt-0.5" />
            <div>
              <h5 class="font-medium text-amber-800 mb-1">Medicamentos con receta</h5>
              <p class="text-amber-700 text-sm mb-3">
                Algunos productos en el pedido requieren receta médica. Valida la receta en los checkboxes de abajo:
              </p>
              
              <!-- List of prescription medications -->
                <ul class="space-y-3 mb-4">
                    <li 
                        v-for="item in cartItems.filter(i => i.requiereReceta)" 
                        :key="item.idProducto" 
                        class="flex items-center justify-between bg-white rounded-md p-3 border border-amber-100"  
                    >
                        <!-- Left side: Product info -->
                        <div class="flex items-center flex-grow">
                        <!-- Product image -->
                        <div v-if="item.image" class="w-10 h-10 rounded overflow-hidden mr-3 flex-shrink-0">
                            <img :src="item.image" :alt="item.nombreProducto" class="w-full h-full object-cover" />
                        </div>
                        
                        <!-- Product name and quantity -->
                        <span class="text-gray-800">
                            {{ item.nombreProducto }} 
                            <span class="text-sm text-gray-500">({{ item.cantidad }})</span>
                        </span>
                        </div>
                        
                        <!-- Right side: Checkbox control -->
                        <div class="flex items-center ml-4 flex-shrink-0">
                        <input 
                            :id="`rx-confirmation-${item.idProducto}`" 
                            type="checkbox" 
                            v-model="prescriptionConfirmations[item.idProducto]"
                            class="rounded border-amber-300 text-blue-600 focus:ring-blue-500 h-7 w-7" 
                        />  
                        </div>
                    </li>
                </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>


