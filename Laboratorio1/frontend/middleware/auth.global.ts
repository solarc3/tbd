import { defineNuxtRouteMiddleware, navigateTo } from "#imports";
import { useAuthStore } from "@/stores/auth";

export default defineNuxtRouteMiddleware(async (to) => {
	const auth = useAuthStore();
	if (!auth.initialized) {
		await auth.initAuth();
	}

	const publicPages = ["/login", "/register", "/clientes"];
	const isPublic = publicPages.includes(to.path);

	if (!auth.isAuthenticated && !isPublic) {
		return navigateTo(`/login?redirect=${encodeURIComponent(to.fullPath)}`);
	}

	if (auth.isAuthenticated && isPublic) {
		return navigateTo("/");
	}
});
