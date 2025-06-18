<script setup lang="ts">
import { ref, watch } from "vue";
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
import MapaSelector from "@/components/MapaSelector.vue"; // Import the map component

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
    latitude: null as number | null, // Add latitude
    longitude: null as number | null, // Add longitude
});

const rutError = ref("");

watch(() => form.value.rut, (newValue) => {
    if (newValue) {
        rutError.value = validateRut(newValue) ? "" : "RUT inválido";
    } else {
        rutError.value = "";
    }
});

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

const handleRutInput = (event: Event) => {
  const target = event.target as HTMLInputElement;
  let value = target.value;

  const cursorPos = target.selectionStart || 0;
  const originalLength = value.length;

  const cleaned = value.replace(/[^0-9kK]/g, "").toUpperCase();
  const formatted = formatRut(cleaned);

  if (form.value.rut !== formatted) {
    form.value.rut = formatted;
  }
  nextTick(() => {
    const newLength = formatted.length;
    let newCursorPos = cursorPos + (newLength - originalLength);
    newCursorPos = Math.max(0, Math.min(newCursorPos, newLength));
    if (document.activeElement === target) {
      target.value = formatted;
      target.setSelectionRange(newCursorPos, newCursorPos);
    }
  });

  // Validate after formatting
  if (formatted && formatted.trim() !== '') {
    rutError.value = validateRut(formatted) ? "" : "RUT inválido";
  } else {
    rutError.value = "";
  }
};

const validateRut = (rut: string): boolean => {
    if (!rut) return false;

    const cleanRut = rut.replace(/[.-]/g, "").toUpperCase();

    // Basic format check: 7 or 8 digits followed by a digit or K
    if (!/^\d{7,8}[0-9K]$/.test(cleanRut)) return false;

    const digits = cleanRut.slice(0, -1);
    const dv = cleanRut.slice(-1); // dv from input

    let sum = 0;
    let multiplier = 2;

    for (let i = digits.length - 1; i >= 0; i--) {
        sum += parseInt(digits[i]) * multiplier;
        multiplier = multiplier === 7 ? 2 : multiplier + 1;
    }

    const remainder = sum % 11;
    const calculatedDV = 11 - remainder;

    let expectedDV; // calculated DV
    if (calculatedDV === 11) expectedDV = "0";
    else if (calculatedDV === 10) expectedDV = "K";
    else expectedDV = calculatedDV.toString();

    return dv === expectedDV;
};

// Handle location selection from the map
const handleLocationSelected = (coords: {
    latitude: number;
    longitude: number;
}) => {
    form.value.latitude = coords.latitude;
    form.value.longitude = coords.longitude;
};

// Validaciones básicas
const validateForm = () => {
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
        return false;
    }

    if (form.value.latitude === null || form.value.longitude === null) { // Add location validation
        error.value = "Por favor, selecciona tu ubicación en el mapa.";
        return false;
    }

    return true;
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
        // Create username from first name + last name initial
        const username = (
            form.value.firstName + form.value.lastName.charAt(0)
        ).toLowerCase();

        // Format RUT to clean format for backend
        const cleanRut = form.value.rut.replace(/[.-]/g, "").toUpperCase();

        await authStore.register(
            username,
            form.value.firstName,
            form.value.lastName,
            cleanRut,
            form.value.email,
            form.value.password,
            form.value.latitude, // Pass latitude
            form.value.longitude // Pass longitude
        );

        success.value = "Cuenta creada exitosamente!";
        toast.success(success.value);

        setTimeout(() => {
            router.push("/login");
        }, 1500);
    } catch (err: any) {
        console.error("Register error:", err);
        error.value = err.response?.data?.message || "Error al crear la cuenta";
        toast.error(error.value);
    } finally {
        isLoading.value = false;
    }
};
</script>

<template>
    <Card class="mx-auto max-w-4xl my-8"> 
        <CardHeader>
            <CardTitle class="text-xl">Sign Up</CardTitle>
            <CardDescription
                >Ingresa tu información para crear una cuenta</CardDescription
            >
        </CardHeader>
        <CardContent>
            <!-- Error and success messages -->
            <div
                v-if="error"
                class="mb-4 p-3 rounded-md bg-red-50 text-red-600 text-sm"
            >
                {{ error }}
            </div>
            <div
                v-if="success"
                class="mb-4 p-3 rounded-md bg-green-50 text-green-600 text-sm"
            >
                {{ success }}
            </div>

            <form @submit.prevent="handleRegister" class="space-y-6">
                <div class="grid md:grid-cols-2 gap-x-8 gap-y-6">
                    
                    <div class="space-y-4 md:space-y-6"> 
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4"> 
                            <div class="space-y-2">
                                <label for="firstName" class="text-sm font-medium"
                                    >Nombre</label
                                >
                                <Input
                                    id="firstName"
                                    v-model="form.firstName"
                                    placeholder="Juan"
                                    required
                                />
                            </div>

                            <div class="space-y-2">
                                <label for="lastName" class="text-sm font-medium"
                                    >Apellido</label
                                >
                                <Input
                                    id="lastName"
                                    v-model="form.lastName"
                                    placeholder="Pérez"
                                    required
                                />
                            </div>
                        </div>

                        <div class="space-y-2">
                            <label for="rut" class="text-sm font-medium">RUT</label>
                            <Input
                                id="rut"
                                v-model="form.rut"
                                placeholder="12.345.678-9"
                                required
                                maxlength="12"
                                @input="handleRutInput"
                            />
                            <p v-if="rutError" class="text-sm text-red-500 mt-1">{{ rutError }}</p>
                        </div>

                        <div class="space-y-2">
                            <label for="email" class="text-sm font-medium"
                                >Correo electrónico</label
                            >
                            <Input
                                id="email"
                                v-model="form.email"
                                type="email"
                                placeholder="correo@ejemplo.com"
                                required
                            />
                        </div>

                        <div class="space-y-2">
                            <label for="password" class="text-sm font-medium"
                                >Contraseña</label
                            >
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

                    <!-- Right Column for the Map -->
                    <div class="flex flex-col space-y-2"> 
                        <label class="block text-sm font-medium">Ubicación</label>
                        <p class="text-xs text-gray-500">
                            Haz clic en el mapa para seleccionar tu ubicación.
                        </p>
                        <div class="flex-1 min-h-[250px] border rounded-md overflow-hidden bg-gray-100">
                            <MapaSelector
                                :initial-lat="-33.4562" 
                                :initial-lng="-70.6483"
                                :initial-zoom="12"
                                @location-selected="handleLocationSelected"
                                class="h-full w-full" 
                            />
                        </div>
                        <div
                            v-if="form.latitude !== null && form.longitude !== null"
                            class="text-xs text-gray-600 mt-1 h-4" 
                        >
                            Lat: {{ form.latitude.toFixed(5) }}, Lng: {{ form.longitude.toFixed(5) }}
                        </div>
                        <div v-else class="h-4 mt-1"/>
                    </div>
                </div>

                <Button
                    type="submit"
                    class="w-full bg-[var(--primary)] transition-colors duration-200 ease-in-out hover:bg-[var(--secondary)]/90"
                    :disabled="isLoading"
                >
                    {{ isLoading ? "Creando cuenta..." : "Crear cuenta" }}
                </Button>
            </form>
            <div class="mt-4 text-center text-sm">
                ¿Ya tienes una cuenta?
                <a href="/login" class="underline">Iniciar sesión</a>
            </div>
        </CardContent>
    </Card>
</template>