<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import {
  Stepper,
  StepperDescription,
  StepperIndicator,
  StepperItem,
  StepperTitle,
  StepperTrigger,
} from '@/components/ui/stepper'
import { Progress } from '@/components/ui/progress'
import { ShoppingCart, CheckCircle, CreditCard, Trash } from 'lucide-vue-next'
import { useCartStore } from '@/stores/cartStore'
import { pedidoService, farmaciaService } from "@/api/services";
import type { FarmaciaEntity, RegistrarPedidoRequest } from "@/api/models";

// https://www.shadcn-vue.com/docs/components/stepper
// https://www.shadcn-vue.com/docs/components/progress.html

const router = useRouter();
const authStore = useAuthStore()
const currentStep = ref(1)
const cartStore = useCartStore();
const progressValue = computed(() => (currentStep.value / 3) * 100);

// carrito,hay q moverlo pq lo deje en pedidos nomas lol
const cartItems = computed(() => cartStore.items);

// resumen de compra considerando que dsps deberia ir el tema del delivery etc
const subtotal = computed(() =>
  cartItems.value.reduce((total, item) => total + item.precio * item.cantidad, 0)
)
const shipping = ref(0)
const total = computed(() => subtotal.value + shipping.value)
const nextStep = () => {
  if (currentStep.value < 3) {
    currentStep.value++
  }
}

const prevStep = () => {
  if (currentStep.value > 1) {
    currentStep.value--
  }
}

watch(currentStep, (newVal) => {
  console.log('Paso actual:', newVal)
})

const showConfirmDialog = ref(false);
const itemIdToRemove = ref<number | null>(null);
const itemNameToRemove = ref<string>('');

// Show confirmation dialog before removing item
const confirmRemoveItem = (id: number, name: string) => {
  itemIdToRemove.value = id;
  itemNameToRemove.value = name;
  showConfirmDialog.value = true;
};

// Confirm and remove item
const handleConfirmRemove = () => {
  if (itemIdToRemove.value !== null) {
    removeItem(itemIdToRemove.value);
    showConfirmDialog.value = false;
    itemIdToRemove.value = null;
  }
};

// Cancel remove action
const cancelRemove = () => {
  showConfirmDialog.value = false;
  itemIdToRemove.value = null;
};

// Cart item actions
const removeItem = (id: number) => {
  cartStore.removeItem(id);
  // Also remove the prescription confirmation for this product
  if (id in prescriptionConfirmations) {
    delete prescriptionConfirmations[id];
  }
};

const incrementQuantity = (id: number) => {
  cartStore.incrementQuantity(id);
};

const decrementQuantity = (id: number) => {
  cartStore.decrementQuantity(id);
};

const hasRxItems = computed(() => {
  return cartItems.value.some(item => item.requiereReceta);
});

// para recetas
const prescriptionConfirmations = reactive<Record<number, boolean>>({})

const initializePrescriptionConfirmations = () => {
  // Find all products that require prescriptions
  const rxItems = cartItems.value.filter(item => item.requiereReceta);
  rxItems.forEach(item => {
    if (prescriptionConfirmations[item.idProducto] === undefined) {
      prescriptionConfirmations[item.idProducto] = false;
    }
  });
};

// Watch for changes in cart items to initialize new prescription items
watch(() => cartItems.value, () => {
  initializePrescriptionConfirmations();
}, { deep: true });

onMounted(() => {
  // Initialize the prescription confirmations when the component mounts
  initializePrescriptionConfirmations();

  fetchFarmacias();
});

// parametros paso 3
const isUrgent = ref(false)
const selectedFarmaciaId = ref(0)
const farmacias = ref<FarmaciaEntity[]>([]);
const fetchFarmacias = async () => {
	try {
		farmacias.value = await farmaciaService.getAllFarmacias();
	} catch (err) {
		console.error("Error fetching farmacias:", err);
	}
};

// Function to handle order placement
const placeOrder = async () => {
  const order : RegistrarPedidoRequest = {
    // mapear a ProductoPedidoRequest (id, cantidad, validada)
    productos: cartItems.value.map(item => ({
      idProducto: item.idProducto,
      cantidad: item.cantidad,
      recetaValidada: prescriptionConfirmations[item.idProducto] !== undefined ? prescriptionConfirmations[item.idProducto] : null,
    })),
    monto: total.value,
    esUrgente: isUrgent.value,
    idFarmacia: selectedFarmaciaId.value,
    idCliente: authStore.user?.id? authStore.user.id : null
  }
  
  console.log('Placing order:', order)
  
  // Call The API to place the order :)
  await pedidoService.completarPedido(order)
    .then(() => {
      alert('¡Pedido completado con éxito!')
      cartStore.clearCart()
      router.push('/profile')
    })
    .catch((error) => {
      console.error('Error placing order:', error)
      alert('Error al realizar el pedido. Intenta nuevamente.')
    })
}


</script>

<template>
  <div class="bg-gray-50 rounded-lg shadow-lg p-8 max-w-4xl mx-auto">
    <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Procedimiento de compra</h2>
    <p class="text-gray-600 mb-8 text-center">Sigue los pasos para completar tu compra de manera rápida y sencilla.</p>

    <!-- dialogo para confirmar eliminar producto -->
    <div v-if="showConfirmDialog" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 max-w-md w-full shadow-xl">
        <div class="flex items-center mb-4 text-amber-600">
          <Trash class="w-6 h-6 mr-2" />
          <h3 class="text-lg font-semibold">Confirmar eliminación</h3>
        </div>
        <p class="mb-6 text-gray-700">
          ¿Estás seguro que deseas eliminar "{{ itemNameToRemove }}"?
        </p>
        <div class="flex justify-end space-x-3">
          <button 
            @click="cancelRemove" 
            class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-100"
          >
            Cancelar
          </button>
          <button 
            @click="handleConfirmRemove" 
            class="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600"
          >
            Eliminar
          </button>
        </div>
      </div>
    </div>

    <!-- stepper !!! -->
    <div class="mb-8">
      <Stepper :current-step="currentStep" class="space-y-6">
        <StepperItem :step="1">
          <StepperTrigger
            class="flex items-center space-x-4"
            :class="{ 'opacity-50': currentStep < 1 }"
          >
            <StepperIndicator
              class="w-10 h-10 flex items-center justify-center rounded-full shadow-md"
              :class="{
                'bg-custom2 text-white': currentStep > 1,
                'bg-custom text-white': currentStep === 1,
                'bg-gray-300 text-gray-500': currentStep < 1
              }"
            >
              <template v-if="currentStep > 1">
                <span class="text-white !bg-transparent !border-none !shadow-none">✔</span>
              </template>
              <template v-else>
                <ShoppingCart class="w-6 h-6" />
              </template>
            </StepperIndicator>
            <div>
              <StepperTitle
                class="text-lg font-semibold"
                :class="{
                  'text-gray-800': currentStep >= 1,
                  'text-gray-500': currentStep < 1
                }"
              >
                Seleccionar productos
              </StepperTitle>
              <StepperDescription class="text-sm text-gray-500">
                Elige los productos que deseas comprar.
              </StepperDescription>
            </div>
          </StepperTrigger>
        </StepperItem>

        <StepperItem :step="2">
          <StepperTrigger
            class="flex items-center space-x-4"
            :class="{ 'opacity-50': currentStep < 2 }"
          >
            <StepperIndicator
              class="w-10 h-10 flex items-center justify-center rounded-full shadow-md"
              :class="{
                'bg-custom2 text-white': currentStep > 2,
                'bg-custom text-white': currentStep === 2,
                'bg-gray-300 text-gray-500': currentStep < 2
              }"
            >
              <template v-if="currentStep > 2">
                <span class="text-white">✔</span>
              </template>
              <template v-else>
                <CheckCircle class="w-6 h-6" />
              </template>
            </StepperIndicator>
            <div>
              <StepperTitle
                class="text-lg font-semibold"
                :class="{
                  'text-gray-800': currentStep >= 2,
                  'text-gray-500': currentStep < 2
                }"
              >
                Confirmar detalles
              </StepperTitle>
              <StepperDescription class="text-sm text-gray-500">
                Revisa y confirma los detalles de tu compra.
              </StepperDescription>
            </div>
          </StepperTrigger>
        </StepperItem>

        <StepperItem :step="3">
          <StepperTrigger
            class="flex items-center space-x-4"
            :class="{ 'opacity-50': currentStep < 3 }"
          >
            <StepperIndicator
              class="w-10 h-10 flex items-center justify-center rounded-full shadow-md"
              :class="{
                'bg-custom2 text-white': currentStep > 3,
                'bg-custom text-white': currentStep === 3,
                'bg-gray-300 text-gray-500': currentStep < 3
              }"
            >
              <template v-if="currentStep > 3">
                ✔
              </template>
              <template v-else>
                <CreditCard class="w-6 h-6" />
              </template>
            </StepperIndicator>
            <div>
              <StepperTitle
                class="text-lg font-semibold"
                :class="{
                  'text-gray-800': currentStep >= 3,
                  'text-gray-500': currentStep < 3
                }"
              >
                Finalizar compra
              </StepperTitle>
              <StepperDescription class="text-sm text-gray-500">
                Completa el pago y finaliza tu compra.
              </StepperDescription>
            </div>
          </StepperTrigger>
        </StepperItem>
      </Stepper>
    </div>

    <div>
    <!-- paso 1 -->
    <div>
      <CarritoStep1
        v-if="currentStep === 1"
        :cart-items="cartItems"
        :subtotal="subtotal"
        :shipping="shipping"
        :total="total"
        @increment-quantity="incrementQuantity"
        @decrement-quantity="decrementQuantity"
        @confirm-remove="confirmRemoveItem"
        @navigate="navigateTo"
      />  
    </div>

      <!-- paso 2 -->
      <div v-if="currentStep === 2" class="mt-8">
        <CarritoStep2
          v-if="currentStep === 2"
          :cart-items="cartItems"
          :total="total"
          :prescription-confirmations="prescriptionConfirmations"
        />
      </div>

      <!-- paso 3 -->
      <div v-if="currentStep === 3" class="mt-8">
        <CarritoStep3
          :total="total"
          :farmacias="farmacias"
          :client-name="authStore.user?.firstName + ' ' + authStore.user?.lastName"
          :client-email="authStore.user?.email"
          v-model:is-urgent="isUrgent"
          v-model:farmacia-id="selectedFarmaciaId"
          @place-order="placeOrder"/>
      </div>
    </div>

    <!-- barra de progreso q no me funciona xd -->
    <div class="mt-8">
      <!-- barra -->
      <div class="relative w-full h-3 bg-gray-200 rounded-full overflow-hidden">
        <div
            class="absolute top-0 left-0 h-full bg-gradient-to-r from-blue-500 to-blue-700 transition-all duration-300"
            :style="{ width: progressValue + '%' }"
        ></div>
      </div>
    </div>

    <!-- botones -->
    <div class="flex justify-between mt-8">
      <button
          class="btn-custom px-6 py-2 text-white bg-gray-500 hover:bg-gray-600 rounded-lg shadow-md transition disabled:opacity-50"
          :disabled="currentStep === 1"
          @click="prevStep"
      >
        Anterior
      </button>
      <button
          class="btn-custom px-6 py-2 text-white bg-custom hover:bg-custom rounded-lg shadow-md transition disabled:opacity-50"
          :disabled="currentStep === 3"
          @click="nextStep"
      >
        Siguiente
      </button>
    </div>
  </div>
</template>