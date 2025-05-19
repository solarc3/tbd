<script setup lang="ts">
import { ShoppingCart, Trash } from 'lucide-vue-next';

defineProps({
  cartItems: {
    type: Array,
    required: true
  },
  subtotal: {
    type: Number,
    required: true
  },
  shipping: {
    type: Number,
    required: true
  },
  total: {
    type: Number,
    required: true
  }
});

const emit = defineEmits([
  'increment-quantity', 
  'decrement-quantity', 
  'confirm-remove',
  'navigate'
]);

const confirmRemove = (id: number, name: string) => {
  emit('confirm-remove', id, name);
};

const incrementQuantity = (id: number) => {
  emit('increment-quantity', id);
};

const decrementQuantity = (id: number) => {
  emit('decrement-quantity', id);
};
</script>

<template>
    <div class="mt-8">
        <h3 class="text-lg font-semibold text-gray-700 mb-4">Productos en el carrito</h3>
    
          <!-- Empty cart message -->
          <div v-if="cartItems.length === 0" class="text-center py-8">
            <ShoppingCart class="w-16 h-16 mx-auto text-gray-400" />
            <p class="mt-4 text-lg text-gray-500">Tu carrito está vacío</p>
            <button 
              @click="$router.push('/productos')" 
              class="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
            >
              Ver productos
            </button>
          </div>
    
          <!-- Cart items list -->
          <div v-else class="space-y-4">
            <div v-for="item in cartItems" :key="item.idProducto" class="flex border rounded-lg p-4 bg-white shadow-sm">
              <!-- Product image -->
              <div class="w-24 h-24 bg-gray-100 rounded-md overflow-hidden flex-shrink-0">
                <img v-if="item.image" :src="item.image" :alt="item.nombreProducto" class="w-full h-full object-cover" />
                <div v-else class="w-full h-full flex items-center justify-center bg-gray-200">
                  <span class="text-gray-400">Sin imagen</span>
                </div>
              </div>
              
              <!-- Product details -->
              <div class="ml-4 flex-grow">
                <h4 class="font-medium text-gray-800">{{ item.nombreProducto }}</h4>
                <div class="mt-2 flex items-center justify-between">
                  <div class="flex items-center space-x-2">
                    <button 
                      @click="decrementQuantity(item.idProducto)"
                      class="w-8 h-8 rounded-full border border-gray-300 flex items-center justify-center"
                    >
                      -
                    </button>
                    <span class="text-gray-700">{{ item.cantidad }}</span>
                    <button 
                      @click="incrementQuantity(item.idProducto)"
                      class="w-8 h-8 rounded-full border border-gray-300 flex items-center justify-center"
                    >
                      +
                    </button>
                  </div>
                  
                  <div class="text-right">
                    <p class="font-medium text-gray-800">${{ (item.precio * item.cantidad).toLocaleString() }}</p>
                    <p class="text-sm text-gray-500">${{ item.precio.toLocaleString() }} c/u</p>
                  </div>
                </div>
              </div>
              
              <!-- Remove button -->
              <button 
                @click="confirmRemove(item.idProducto, item.nombreProducto)" 
                class="ml-2 text-red-500"
                title="Eliminar"
              >
                <span class="sr-only">Eliminar</span>
                <Trash class="w-6 h-6" />
              </button>
            </div>
            
            <!-- Order summary -->
            <div class="mt-6 border-t pt-4">
              <div class="flex justify-between py-1">
                <span class="text-gray-600">Subtotal</span>
                <span class="font-medium">${{ subtotal.toLocaleString() }}</span>
              </div>
              <div class="flex justify-between py-1">
                <span class="text-gray-600">Envío</span>
                <span class="font-medium">${{ shipping.toLocaleString() }}</span>
              </div>
              <div class="flex justify-between py-1 font-semibold text-lg">
                <span>Total</span>
                <span>${{ total.toLocaleString() }}</span>
              </div>
            </div>
          </div>
      </div>

</template>