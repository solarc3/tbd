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

const router = useRouter();
const authStore = useAuthStore();
const error = ref("");
const success = ref("");
const isLoading = ref(false);

// Datos del formulario
const form = ref({
	firstName: "",
	lastName: "",
	rut: "",
	email: "",
	password: "",
	confirmPassword: "",
});

const rutError = ref("");

// Format RUT as the user types (XX.XXX.XXX-X)
const formatRut = (value: string): string => {
    // Remove all non-alphanumeric characters
    let cleaned = value.replace(/[^0-9kK]/g, "").toUpperCase();

    if (cleaned.length === 0) return "";

    // Format the RUT
    let result = "";

    // Add the verification digit with a dash
    if (cleaned.length > 1) {
        const body = cleaned.slice(0, -1);
        const dv = cleaned.slice(-1);
        cleaned = body + "-" + dv;
    }

    // Add dots for thousands
    const parts = cleaned.split("-");
    let num = parts[0];

    // Add dots
    for (let i = num.length - 3; i > 0; i -= 3) {
        num = num.slice(0, i) + "." + num.slice(i);
    }

    result = parts.length > 1 ? num + "-" + parts[1] : num;

    return result;
};

// Watch for RUT changes and format it
watch(() => form.value.rut, (newValue) => {
    // Only format if not empty and if the last character isn't a dot or dash
    // This prevents cursor jumping while typing
    if (newValue && !newValue.endsWith(".") && !newValue.endsWith("-")) {
        // Remember cursor position
        const input = document.getElementById("rut") as HTMLInputElement;
        const cursorPos = input?.selectionStart || 0;
        const oldLength = newValue.length;

        // Format RUT
        const formatted = formatRut(newValue);

        // Only update if formatting actually changed something
        if (formatted !== newValue) {
            form.value.rut = formatted;

            // Restore cursor position, accounting for added characters
            setTimeout(() => {
                if (input) {
                    const newPos = cursorPos + (formatted.length - oldLength);
                    input.setSelectionRange(newPos, newPos);
                }
            }, 0);
        }
    }

    // Validate in real-time
    if (newValue) {
        rutError.value = validateRut(newValue) ? "" : "RUT inválido";
    } else {
        rutError.value = "";
    }
});

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

	return true;
};

const handleRegister = async () => {
	error.value = "";
	success.value = "";

	if (!validateForm()) {
		toast.error(error.value);
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
	<Card class="mx-auto max-w-sm">
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

			<form @submit.prevent="handleRegister" class="grid gap-4">
				<div class="grid grid-cols-2 gap-4">
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