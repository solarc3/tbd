import { defineNuxtRouteMiddleware, navigateTo } from "#imports";
import { useAuthStore } from "@/stores/auth";

export default defineNuxtRouteMiddleware(async (to) => {
	const auth = useAuthStore();
	await auth.initAuth();

	const publicPages = ["/login", "/register", "/clientes"];
	const isPublic = publicPages.includes(to.path);

	if (!auth.isAuthenticated && !isPublic) {
		return navigateTo("/login");
	}

	if (auth.isAuthenticated && isPublic) {
		return navigateTo("/");
	}
});
