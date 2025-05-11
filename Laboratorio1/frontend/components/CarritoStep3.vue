<script setup lang="ts">
import { ref, computed } from 'vue';
import { CreditCard, MapPin, User, Clock } from 'lucide-vue-next';

const props = defineProps({
  total: {
    type: Number,
    required: true
  },
  farmacias: {
    type: Array,
    default: () => []
  },
  clientName: {
    type: String,
    default: 'Usuario'
  },
  clientEmail: {
    type: String,
    default: 'ejemplo@mail.com'
  }
});

const emit = defineEmits(['update:isUrgent', 'update:farmaciaId', 'placeOrder']);

// Local state
const isUrgent = ref(false);
const selectedFarmacia = ref(props.farmacias.length > 0 ? props.farmacias[0].id : null);
const termsAccepted = ref(false);

// Update parent component when values change
const updateIsUrgent = (value: boolean) => {
  isUrgent.value = value;
  emit('update:isUrgent', value);
};

// Watch for farmacias changes and set default when available
watch(() => props.farmacias, (newFarmacias) => {
  if (newFarmacias.length > 0 && !selectedFarmacia.value) {
    // Set the first farmacia as default
    selectedFarmacia.value = newFarmacias[0].idFarmacia;
    // Also emit to parent to update the v-model
    emit('update:farmaciaId', newFarmacias[0].idFarmacia);
  }
}, { immediate: true });

const updateFarmacia = (id: number) => {
  selectedFarmacia.value = id;
  emit('update:farmaciaId', id);
};

const placeOrder = () => {
  emit('placeOrder');
};

const finalTotal = computed(() => props.total);
</script>
<template>
    <div class="mt-8">
      <h3 class="text-lg font-semibold text-gray-700 mb-4">Finalizar pedido</h3>
      
      <!-- Order details form -->
      <div class="bg-white rounded-lg shadow-sm p-6 border mb-6">
        <h4 class="font-medium text-gray-800 mb-4 pb-2 border-b flex items-center">
          <User class="w-5 h-5 mr-2 text-gray-600" />
          Información del cliente
        </h4>
        
        <!-- Client information -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
          <div class="p-3 border rounded-md bg-gray-50">
            <p class="text-sm text-gray-500">Nombre del cliente</p>
            <p class="font-medium">{{ clientName }}</p>
          </div>
          
          <div class="p-3 border rounded-md bg-gray-50">
            <p class="text-sm text-gray-500">Email</p>
            <p class="font-medium">{{ clientEmail }}</p>
          </div>
        </div>
        
        <!-- Delivery options -->
        <h4 class="font-medium text-gray-800 mb-4 pb-2 border-b flex items-center mt-8">
          <MapPin class="w-5 h-5 mr-2 text-gray-600" />
          Farmacia de retiro
        </h4>
        
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Selecciona la farmacia donde se retirará el pedido
          </label>
          <select 
            v-model="selectedFarmacia" 
            @change="updateFarmacia($event.target.value)"
            class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
          >
            <option v-for="farmacia in farmacias" :key="farmacia.idFarmacia" :value="farmacia.idFarmacia">
              {{ farmacia.nombreFarmacia }} - {{ farmacia.direccion }}
            </option>
            <option v-if="farmacias.length === 0" disabled>No hay farmacias disponibles</option>
          </select>
        </div>
        
        <!-- Urgent order option -->
        <h4 class="font-medium text-gray-800 mb-4 pb-2 border-b flex items-center mt-8">
          <Clock class="w-5 h-5 mr-2 text-gray-600" />
          Opciones de entrega
        </h4>
        
        <div class="flex items-center p-4 bg-blue-50 rounded-md border border-blue-100 mb-6">
          <input 
            type="checkbox" 
            id="urgent-order" 
            v-model="isUrgent" 
            @change="updateIsUrgent(isUrgent)"
            class="h-5 w-5 text-blue-600 rounded border-gray-300 focus:ring-blue-500"
          />
          <div class="ml-3">
            <label for="urgent-order" class="font-medium text-gray-800">Pedido urgente</label>
            <p class="text-sm text-gray-600">
              Los pedidos urgentes tienen prioridad y son preparados en el menor tiempo posible.
            </p>
          </div>
        </div>
        
        <!-- Payment summary -->
        <h4 class="font-medium text-gray-800 mb-4 pb-2 border-b flex items-center mt-8">
          <CreditCard class="w-5 h-5 mr-2 text-gray-600" />
          Resumen de pago
        </h4>
        
        <div class="flex justify-between py-2 mt-2 font-bold text-lg">
            <span>Total a pagar</span>
            <span>${{ finalTotal.toLocaleString() }}</span>
        </div>
      </div>
      
      <!-- Place order button -->
      <div class="mt-6">
        <button 
          @click="placeOrder"
          class="w-full py-3 bg-blue-600 text-white font-medium rounded-md shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Crear Pedido
        </button>
      </div>
    </div>
  </template>