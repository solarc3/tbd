<script setup lang="ts">
import { ref } from "vue";
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
const email = ref("");
const password = ref("");

const handleLogin = async () => {
	error.value = "";

	if (!email.value || !password.value) {
		error.value = "Por favor ingresa tu correo electrónico y contraseña";
		toast.error(error.value);
		return;
	}

	try {
		await authStore.login(email.value, password.value);
		toast.success("Inicio de sesión exitoso");
		router.push("/");
	} catch (err: any) {
		console.error("Login error:", err);
		error.value =
			err.response?.data?.message ||
			"Error al iniciar sesión, intenta nuevamente";
		toast.error(error.value);
	}
};
</script>

<template>
	<div
		class="w-full min-h-screen flex items-center justify-center px-4 py-8 bg-gray-50"
	>
		<div class="w-full max-w-sm sm:max-w-md md:max-w-lg lg:max-w-xl">
			<Card>
				<CardHeader>
					<CardTitle class="text-2xl">Login</CardTitle>
					<CardDescription
						>Ingresa tu correo electrónico y contraseña para entrar
						a tu cuenta</CardDescription
					>
				</CardHeader>
				<CardContent>
					<form @submit.prevent="handleLogin">
						<div
							v-if="error"
							class="p-3 mb-4 rounded-md bg-red-50 text-red-600 text-sm"
						>
							{{ error }}
						</div>

						<div class="grid gap-4">
							<div class="space-y-2">
								<label for="email" class="text-sm font-medium"
									>Correo electrónico</label
								>
								<Input
									id="email"
									v-model="email"
									type="email"
									placeholder="correo@ejemplo.com"
									required
									autocomplete="email"
								/>
							</div>

							<div class="space-y-2">
								<div class="flex items-center">
									<label
										for="password"
										class="text-sm font-medium"
										>Contraseña</label
									>
									<a
										href="#"
										class="ml-auto text-sm underline"
										>Olvidaste tu contraseña?</a
									>
								</div>
								<Input
									id="password"
									v-model="password"
									type="password"
									required
									autocomplete="current-password"
								/>
							</div>

							<Button
								type="submit"
								:disabled="authStore.isLoading"
								class="w-full bg-[var(--primary)] transition-colors duration-200 ease-in-out hover:bg-[var(--secondary)]/20"
							>
								{{
									authStore.isLoading
										? "Iniciando sesión..."
										: "Login"
								}}
							</Button>
						</div>
					</form>

					<div class="mt-4 text-center text-sm">
						No tienes una cuenta aun?
						<a href="/register" class="underline">Registrate</a>
					</div>
				</CardContent>
			</Card>
		</div>
	</div>
</template>
