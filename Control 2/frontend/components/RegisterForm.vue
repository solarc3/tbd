<script setup lang="ts">
import { ref, watch, nextTick } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { toast } from "vue-sonner";
import MapaSelector from '@/components/MapaTareas.vue';

const router = useRouter();
const authStore = useAuthStore();
const error = ref("");
const success = ref("");
const isLoading = ref(false);

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

const rutError = ref("");
const mapComponentRef = ref<InstanceType<typeof MapaSelector> | null>(null);

const formatRut = (value: string): string => {
  let cleaned = value.replace(/[^0-9kK]/g, "").toUpperCase();
  if (cleaned.length === 0) return "";
  if (cleaned.length > 1) {
    const body = cleaned.slice(0, -1);
    const dv = cleaned.slice(-1);
    cleaned = body + "-" + dv;
  }
  const parts = cleaned.split("-");
  let num = parts[0];
  for (let i = num.length - 3; i > 0; i -= 3) {
    num = num.slice(0, i) + "." + num.slice(i);
  }
  return parts.length > 1 ? num + "-" + parts[1] : num;
};

watch(() => form.value.rut, (newValue, oldValue) => {
  const input = document.getElementById("rut") as HTMLInputElement;
  if (!input) return;
  const cursorPos = input.selectionStart || 0;
  const originalLength = newValue.length;

  const formatted = formatRut(newValue);
  if (formatted !== newValue) {
    form.value.rut = formatted;
    nextTick(() => {
      const newLength = formatted.length;
      const diff = newLength - originalLength;
      input.setSelectionRange(cursorPos + diff, cursorPos + diff);
    });
  }
  if (newValue) {
    rutError.value = validateRut(newValue) ? "" : "RUT inválido";
  } else {
    rutError.value = "";
  }
}, { immediate: false });

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

const validateForm = () => {
  error.value = "";
  if (!form.value.firstName || !form.value.lastName || !form.value.rut || !form.value.email || !form.value.password || !form.value.confirmPassword) {
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


const handleLocationSelected = (coords: { latitude: number; longitude: number }) => {
  form.value.latitude = coords.latitude;
  form.value.longitude = coords.longitude;
  console.log("[RegisterForm] Coordenadas seleccionadas actualizadas en el formulario:", form.value.latitude, form.value.longitude);
};

const handleRegister = async () => {
  error.value = "";
  success.value = "";

  if (!validateForm()) {
    toast.error(error.value || "Por favor revisa los campos del formulario.");
    return;
  }

  console.log("[RegisterForm] Enviando Latitud:", form.value.latitude, typeof form.value.latitude);
  console.log("[RegisterForm] Enviando Longitud:", form.value.longitude, typeof form.value.longitude);

  try {
    isLoading.value = true;
    const username = (
        form.value.firstName + form.value.lastName.charAt(0)
    ).toLowerCase().replace(/\s+/g, '');

    const cleanRut = form.value.rut.replace(/[.-]/g, "").toUpperCase();

    await authStore.register(
        username,
        form.value.firstName,
        form.value.lastName,
        cleanRut,
        form.value.email,
        form.value.password,
        form.value.latitude,
        form.value.longitude,
    );

    success.value = "Cuenta creada exitosamente!";
    toast.success(success.value);

    setTimeout(() => {
      router.push("/login");
    }, 1500);
  } catch (err: any) {
    console.error("Error de Registro:", err);
    error.value = err.response?.data?.message || "Error al crear la cuenta. Intenta de nuevo.";
    toast.error(error.value);
  } finally {
    isLoading.value = false;
  }
};
</script>

<template>
  <Card class="mx-auto max-w-sm my-8"> <CardHeader>
    <CardTitle class="text-xl">Registrate</CardTitle>
    <CardDescription>Ingresa tu información para crear una cuenta nueva.</CardDescription>
  </CardHeader>
    <CardContent>
      <form @submit.prevent="handleRegister" class="grid gap-4">
        <div class="grid grid-cols-2 gap-4">
          <div class="space-y-2">
            <label for="firstName" class="text-sm font-medium">Nombre</label>
            <Input id="firstName" v-model="form.firstName" placeholder="Juan" required />
          </div>
          <div class="space-y-2">
            <label for="lastName" class="text-sm font-medium">Apellido</label>
            <Input id="lastName" v-model="form.lastName" placeholder="Pérez" required />
          </div>
        </div>

        <div class="space-y-2">
          <label for="rut" class="text-sm font-medium">RUT</label>
          <Input id="rut" v-model="form.rut" placeholder="12.345.678-9" required />
          <p v-if="rutError" class="text-sm text-red-500 mt-1">{{ rutError }}</p>
        </div>

        <div class="space-y-2">
          <label for="email" class="text-sm font-medium">Correo electrónico</label>
          <Input id="email" v-model="form.email" type="email" placeholder="correo@ejemplo.com" required />
        </div>

        <div class="space-y-2">
          <label class="text-sm font-medium">Ubicacion</label>
          <p class="text-xs text-gray-500 mb-2">Haz clic en el mapa para seleccionar tu ubicacion.</p>
          <div class="h-64 w-full border rounded-md overflow-hidden bg-gray-100">
            <MapaSelector
                ref="mapComponentRef"
                :initialLat="-33.4562" لهواء :initialLng="-70.6483"
                :initialZoom="12"
                :selectedLatitude="form.latitude"
                :selectedLongitude="form.longitude"
                @location-selected="handleLocationSelected"
            />
          </div>
          <div v-if="form.latitude !== null && form.longitude !== null" class="text-xs text-gray-600 mt-1">
            Lat: {{ form.latitude.toFixed(5) }}, Lng: {{ form.longitude.toFixed(5) }}
          </div>
        </div>

        <div class="space-y-2">
          <label for="password" class="text-sm font-medium">Contraseña</label>
          <Input id="password" v-model="form.password" type="password" required />
        </div>

        <div class="space-y-2">
          <label for="confirmPassword" class="text-sm font-medium">Confirmar Contraseña</label>
          <Input id="confirmPassword" v-model="form.confirmPassword" type="password" required />
        </div>

        <Button
            type="submit"
            class="w-full bg-[var(--primary)] transition-colors duration-200 ease-in-out hover:bg-[var(--secondary)]/90 text-white"
            :disabled="isLoading"
        >
          {{ isLoading ? "Creando cuenta..." : "Crear cuenta" }}
        </Button>
      </form>
      <div class="mt-4 text-center text-sm">
        ¿Ya tienes una cuenta?
        <router-link to="/login" class="underline">Iniciar sesión</router-link> </div>
    </CardContent>
  </Card>
</template>