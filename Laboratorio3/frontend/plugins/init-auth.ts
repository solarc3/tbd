import { defineNuxtPlugin } from "#imports";
import { useAuthStore } from "@/stores/auth";

export default defineNuxtPlugin(async () => {
	const auth = useAuthStore();
	await auth.initAuth();
});