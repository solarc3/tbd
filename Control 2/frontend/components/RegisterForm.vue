<template>
  <Card class="mx-auto max-w-4xl my-8">
    <CardHeader>
      <CardTitle class="text-xl">Regístrate</CardTitle>
      <CardDescription>
        Ingresa tu información para crear una cuenta nueva.
      </CardDescription>
    </CardHeader>
    <CardContent>
      <form class="space-y-6" @submit.prevent="handleRegister">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div class="space-y-2">
            <label for="firstName" class="text-sm font-medium">Nombre</label>
            <Input
                id="firstName"
                v-model="form.firstName"
                placeholder="Juan"
                required
            />
          </div>
          <div class="space-y-2">
            <label for="lastName" class="text-sm font-medium">Apellido</label>
            <Input
                id="lastName"
                v-model="form.lastName"
                placeholder="Pérez"
                required
            />
          </div>
          <div class="space-y-2">
            <label for="rut" class="text-sm font-medium">RUT</label>
            <Input
                id="rut"
                v-model="form.rut"
                placeholder="12.345.678-9"
                required
                :maxlength="12"
                @input="handleRutInput"
            />
            <div class="h-4 mt-1">
              <p v-if="rutError" class="text-sm text-red-500">{{ rutError }}</p>
            </div>
          </div>
        </div>

        <div class="space-y-2">
          <label for="email" class="text-sm font-medium">Correo electrónico</label>
          <Input
              id="email"
              v-model="form.email"
              type="email"
              placeholder="correo@ejemplo.com"
              required
          />
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 h-80">
          <div class="flex flex-col h-full">
            <label class="text-sm font-medium">Ubicación</label>
            <p class="text-xs text-gray-500 mb-2">
              Haz clic en el mapa para seleccionar tu ubicación.
            </p>
            <div class="flex-1 border rounded-md overflow-hidden bg-gray-100">
              <MapaSelector
                  ref="mapComponentRef"
                  :initial-lat="-33.4562"
                  :initial-lng="-70.6483"
                  :initial-zoom="12"
                  :selected-latitude="form.latitude"
                  :selected-longitude="form.longitude"
                  @location-selected="handleLocationSelected"
              />
            </div>
            <div
                v-if="form.latitude !== null && form.longitude !== null"
                class="text-xs text-gray-600 mt-2 h-4"
            >
              Lat: {{ form.latitude.toFixed(5) }}, Lng: {{ form.longitude.toFixed(5) }}
            </div>
            <div v-else class="h-4 mt-2"/>
          </div>

          <div class="flex flex-col justify-between h-full">
            <div class="space-y-4">
              <div class="space-y-2">
                <label for="password" class="text-sm font-medium">Contraseña</label>
                <Input
                    id="password"
                    v-model="form.password"
                    type="password"
                    required
                />
              </div>

              <div class="space-y-2">
                <label for="confirmPassword" class="text-sm font-medium"
                >Confirmar Contraseña</label
                >
                <Input
                    id="confirmPassword"
                    v-model="form.confirmPassword"
                    type="password"
                    required
                />
              </div>
            </div>

            <div class="mt-auto">
              <Button
                  type="submit"
                  class="w-full bg-[var(--primary)] transition-colors duration-200 ease-in-out hover:bg-[var(--secondary)]/90 text-white"
                  :disabled="isLoading"
              >
                {{ isLoading ? "Creando cuenta..." : "Crear cuenta" }}
              </Button>
            </div>
          </div>
        </div>
      </form>

      <div class="mt-6 text-center text-sm">
        ¿Ya tienes una cuenta?
        <NuxtLink to="/login" class="underline">Iniciar sesión</NuxtLink>
      </div>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import { ref, watch, nextTick } from "vue";
import { navigateTo } from "#app";
import { useAuthStore } from "~/stores/auth";
import { Button } from "~/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "~/components/ui/card";
import { Input } from "~/components/ui/input";
import { toast } from "vue-sonner";
import MapaSelector from "~/components/MapaTareas.vue";

const authStore = useAuthStore();
const error = ref<string>("");
const success = ref<string>("");
const isLoading = ref<boolean>(false);

const form = ref({
  firstName: "",
  lastName: "",
  rut: "",
  email: "",
  password: "",
  confirmPassword: "",
  latitude: null as number | null,
  longitude: null as number | null,
});

const rutError = ref<string>("");
const mapComponentRef = ref<InstanceType<typeof MapaSelector> | null>(null);


watch(
    () => form.value.rut,
    (newValue) => {
      if (newValue) {
        rutError.value = validateRut(newValue) ? "" : "RUT inválido";
      } else {
        rutError.value = "";
      }
    },
    { immediate: false }
);

const handleRutInput = (event: Event) => {
  const target = event.target as HTMLInputElement;
  const value = target.value;

  const cleaned = value.replace(/[^0-9kK]/g, "").toUpperCase();

  let finalValue = value;

  if (value !== cleaned) {
    const formatted = formatRut(cleaned);
    form.value.rut = formatted;
    finalValue = formatted;

    nextTick(() => {
      target.setSelectionRange(formatted.length, formatted.length);
    });
  } else {
    const formatted = formatRut(cleaned);
    if (formatted !== value) {
      form.value.rut = formatted;
      finalValue = formatted;

      nextTick(() => {
        const cursorPos = target.selectionStart || 0;
        const diff = formatted.length - value.length;
        target.setSelectionRange(cursorPos + diff, cursorPos + diff);
      });
    }
  }

  if (finalValue && finalValue.trim() !== '') {
    rutError.value = validateRut(finalValue) ? "" : "RUT inválido";
  } else {
    rutError.value = "";
  }
};

const formatRut = (value: string): string => {
  const cleaned = value.replace(/[^0-9kK]/g, "").toUpperCase();

  if (cleaned.length === 0) return "";

  if (cleaned.length === 1) return cleaned;

  const body = cleaned.slice(0, -1);
  const dv = cleaned.slice(-1);

  let formattedBody = body;
  if (body.length > 3) {
    formattedBody = body.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
  }

  return formattedBody + "-" + dv;
};


const validateRut = (rut: string): boolean => {
  if (!rut) return false;

  const cleanRut = rut.replace(/[.-]/g, "").toUpperCase();

  if (!/^\d{7,8}[0-9K]$/.test(cleanRut)) return false;

  const digits = cleanRut.slice(0, -1);
  const dv = cleanRut.slice(-1);

  let sum = 0;
  let multiplier = 2;

  for (let i = digits.length - 1; i >= 0; i--) {
    sum += parseInt(digits[i]) * multiplier;
    multiplier = multiplier === 7 ? 2 : multiplier + 1;
  }

  const remainder = sum % 11;
  const calculatedDV = 11 - remainder;

  let expectedDV;
  if (calculatedDV === 11) expectedDV = "0";
  else if (calculatedDV === 10) expectedDV = "K";
  else expectedDV = calculatedDV.toString();

  return dv === expectedDV;
};

const validateForm = (): boolean => {
  error.value = "";
  if (
      !form.value.firstName ||
      !form.value.lastName ||
      !form.value.rut ||
      !form.value.email ||
      !form.value.password ||
      !form.value.confirmPassword
  ) {
    error.value = "Por favor completa todos los campos requeridos";
    return false;
  }
  if (form.value.password !== form.value.confirmPassword) {
    error.value = "Las contraseñas no coinciden";
    return false;
  }
  if (!validateRut(form.value.rut)) {
    error.value = "El RUT ingresado no es válido";
    rutError.value = "RUT inválido";
    return false;
  }
  if (form.value.latitude === null || form.value.longitude === null) {
    error.value = "Por favor, selecciona tu ubicación en el mapa.";
    return false;
  }
  return true;
};

const handleLocationSelected = (coords: {
  latitude: number;
  longitude: number;
}) => {
  form.value.latitude = coords.latitude;
  form.value.longitude = coords.longitude;
  console.log(
      "[RegisterForm] Coordenadas seleccionadas actualizadas en el formulario:",
      form.value.latitude,
      form.value.longitude
  );
};

const handleRegister = async () => {
  error.value = "";
  success.value = "";

  if (!validateForm()) {
    toast.error(error.value || "Por favor revisa los campos del formulario.");
    return;
  }


  try {
    isLoading.value = true;
    const username = (
        form.value.firstName + form.value.lastName.charAt(0)
    )
        .toLowerCase()
        .replace(/\s+/g, "");

    const cleanRut = form.value.rut.replace(/[.-]/g, "").toUpperCase();

    await authStore.register(
        username,
        form.value.firstName,
        form.value.lastName,
        cleanRut,
        form.value.email,
        form.value.password,
        form.value.latitude,
        form.value.longitude
    );

    success.value = "Cuenta creada exitosamente!";
    toast.success(success.value);

    setTimeout(() => {
      navigateTo("/login");
    }, 1500);
  } catch (err: any) {
    console.error("Error de Registro:", err);
    error.value =
        err.response?.data?.message || "Error al crear la cuenta. Intenta de nuevo.";
    toast.error(error.value);
  } finally {
    isLoading.value = false;
  }
};
</script>
