<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { Button } from "@/components/ui/button";
import {
	Card,
	CardContent,
	CardDescription,
	CardHeader,
	CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const authStore = useAuthStore();
const error = ref("");
const success = ref("");

const form = ref({
	firstName: "",
	lastName: "",
	rut: "",
	email: "",
	password: "",
	confirmPassword: "",
});

const isLoading = ref(false);

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

const handleRegister = async () => {
	error.value = "";
	success.value = "";

	if (
		!form.value.firstName ||
		!form.value.lastName ||
		!form.value.email ||
		!form.value.password ||
		!form.value.rut
	) {
		error.value = "Por favor completa todos los campos requeridos";
		return;
	}

	if (form.value.password !== form.value.confirmPassword) {
		error.value = "Las contraseñas no coinciden";
		return;
	}

	// Validate RUT
	if (!validateRut(form.value.rut)) {
		error.value = "El RUT ingresado no es válido";
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
		setTimeout(() => {
			router.push("/login");
		}, 1500);
	} catch (err: any) {
		console.error("Register error:", err);
		error.value = err.response?.data?.message || "Error al crear la cuenta";
	} finally {
		isLoading.value = false;
	}
};
</script>

<template>
	<Card class="mx-auto max-w-sm">
		<CardHeader>
			<CardTitle class="text-xl"> Sign Up </CardTitle>
			<CardDescription>
				Ingresa tu información para crear una cuenta
			</CardDescription>
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
					<div class="grid gap-2">
						<Label for="first-name">Nombre</Label>
						<Input
							id="first-name"
							v-model="form.firstName"
							placeholder="Juan"
							required
						/>
					</div>
					<div class="grid gap-2">
						<Label for="last-name">Apellido</Label>
						<Input
							id="last-name"
							v-model="form.lastName"
							placeholder="Pérez"
							required
						/>
					</div>
				</div>
				<div class="grid gap-2">
					<Label for="rut">RUT</Label>
					<Input
						id="rut"
						v-model="form.rut"
						placeholder="12.345.678-9"
						required
					/>
				</div>
				<div class="grid gap-2">
					<Label for="email">Correo electrónico</Label>
					<Input
						id="email"
						type="email"
						v-model="form.email"
						placeholder="correo@ejemplo.com"
						required
					/>
				</div>
				<div class="grid gap-2">
					<Label for="password">Contraseña</Label>
					<Input
						id="password"
						type="password"
						v-model="form.password"
						required
					/>
				</div>
				<div class="grid gap-2">
					<Label for="confirm-password">Confirmar Contraseña</Label>
					<Input
						id="confirm-password"
						type="password"
						v-model="form.confirmPassword"
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
				<a href="/login" class="underline"> Iniciar sesión </a>
			</div>
		</CardContent>
	</Card>
</template>
