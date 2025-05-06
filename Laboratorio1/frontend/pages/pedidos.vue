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
import { ShoppingCart, CheckCircle, CreditCard } from 'lucide-vue-next'

// https://www.shadcn-vue.com/docs/components/stepper
// https://www.shadcn-vue.com/docs/components/progress.html

const currentStep = ref(1)

// carrito,hay q moverlo pq lo deje en pedidos nomas lol
const cartItems = ref([
  { id: 1, name: 'Producto A', price: 10000, quantity: 2 },
  { id: 2, name: 'Producto B', price: 15000, quantity: 1 },
])

// resumen de compra considerando que dsps deberia ir el tema del delivery etc
const subtotal = computed(() =>
  cartItems.value.reduce((total, item) => total + item.price * item.quantity, 0)
)
const shipping = ref(5000)
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
</script>

<template>
  <div class="bg-gray-50 rounded-lg shadow-lg p-8 max-w-4xl mx-auto">
    <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Procedimiento de compra</h2>
    <p class="text-gray-600 mb-8 text-center">Sigue los pasos para completar tu compra de manera rápida y sencilla.</p>

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

    <!-- paso 1 -->
    <div>
      <div v-if="currentStep === 1" class="mt-8">
        <h3 class="text-lg font-semibold text-gray-700 mb-4">Productos en el carrito</h3>
      </div>

      <!-- paso 2 -->
      <div v-if="currentStep === 2" class="mt-8">
        <h3 class="text-lg font-semibold text-gray-700 mb-4">Confirmar detalles</h3>
      </div>

      <!-- paso 3 -->
      <div v-if="currentStep === 3" class="mt-8">
        <h3 class="text-lg font-semibold text-gray-700 mb-4">Finalizar compra</h3>
      </div>
    </div>

    <!-- barra de progreso q no me funciona xd -->
    <div class="mt-8">
      <Progress :model-value="(currentStep / 3) * 100" class="h-3 bg-gray-200 rounded-full">
        <div class="h-full bg-gradient-to-r from-blue-500 to-blue-700 rounded-full"></div>
      </Progress>
    </div>

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